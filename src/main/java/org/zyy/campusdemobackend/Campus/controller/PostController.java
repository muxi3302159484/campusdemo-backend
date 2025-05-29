package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zyy.campusdemobackend.Campus.service.PostService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 发布动态
    @PostMapping
    public Map<String, Object> createPost(@RequestBody Map<String, Object> postData) {
        return postService.createPost(postData);
    }

    // 分页获取动态列表，支持标签筛选
    @GetMapping
    public Map<String, Object> getPosts(@RequestParam int page, @RequestParam int pageSize,
                                        @RequestParam(required = false) String tag) {
        return postService.getPosts(page, pageSize, tag);
    }

    // 点赞动态，支持 userId
    @PostMapping("/{postId}/like")
    public Map<String, Object> likePost(@PathVariable Integer postId, @RequestBody Map<String, Object> payload) {
        Integer userId = (Integer) payload.get("userId");
        return postService.likePost(postId, userId);
    }

    // 评论动态，支持 userId
    @PostMapping("/{postId}/comments")
    public Map<String, Object> commentPost(@PathVariable Integer postId, @RequestBody Map<String, Object> payload) {
        String content = (String) payload.get("content");
        Integer userId = (Integer) payload.get("userId");
        return postService.commentPost(postId, content, userId);
    }

    // 图片上传接口
    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        if (file.isEmpty()) {
            return Map.of("code", 400, "message", "文件为空", "data", null);
        }
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (ext == null || !List.of("jpg", "jpeg", "png", "gif").contains(ext.toLowerCase())) {
            return Map.of("code", 400, "message", "不支持的文件类型", "data", null);
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            return Map.of("code", 400, "message", "文件过大", "data", null);
        }
        String filename = UUID.randomUUID() + "." + ext;
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        File dest = new File(dir, filename);
        file.transferTo(dest);

        // 审核图片内容
        try {
            var resp = org.zyy.campusdemobackend.Campus.Util.TencentImageModerationUtil.checkImage(dest.getAbsolutePath());
            String suggestion = resp.getSuggestion();
            String label = resp.getLabel();
            if (!"Pass".equalsIgnoreCase(suggestion)) {
                dest.delete(); // 删除违规图片
                return Map.of(
                        "code", 403,
                        "message", "图片内容违规",
                        "data", Map.of(
                                "reason", label,
                                "suggestion", suggestion
                        )
                );
            }
        } catch (Exception e) {
            dest.delete();
            return Map.of("code", 500, "message", "图片内容审核失败", "data", null);
        }

        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/photos/" + filename;
        return Map.of("code", 200, "message", "上传成功", "data", Map.of("url", url));
    }
}