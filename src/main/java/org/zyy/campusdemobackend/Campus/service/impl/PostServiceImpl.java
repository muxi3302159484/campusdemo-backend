package org.zyy.campusdemobackend.Campus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zyy.campusdemobackend.Campus.model.Post;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.model.Comment;
import org.zyy.campusdemobackend.Campus.model.Like;
import org.zyy.campusdemobackend.Campus.repository.*;
import org.zyy.campusdemobackend.Campus.service.PostService;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public Map<String, Object> createPost(Map<String, Object> postData) {
        try {
            Integer userId = (Integer) postData.get("userId");
            if (userId == null) {
                return Map.of("success", false, "message", "userId 不能为空");
            }
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

            String content = (String) postData.get("content");
            // 1. 内容安全审核
            try {
                com.tencentcloudapi.tms.v20201229.models.TextModerationResponse resp =
                        org.zyy.campusdemobackend.Campus.Util.TencentTextModerationUtil.checkText(content);
                String suggestion = resp.getSuggestion();
                if ("Block".equalsIgnoreCase(suggestion)) {
                    return Map.of("success", false, "message", "您的贴文内容违规，无法发布。");
                }
            } catch (com.tencentcloudapi.common.exception.TencentCloudSDKException e) {
                return Map.of("success", false, "message", "内容审核服务异常，请稍后再试。");
            }

            // 2. 审核通过，正常入库
            Post post = new Post();
            post.setContent(content);

            // 兼容 images 字段为 List、数组或单个字符串
            Object imagesObj = postData.get("images");
            List<String> images = new ArrayList<>();
            if (imagesObj instanceof List) {
                for (Object img : (List<?>) imagesObj) {
                    if (img != null) images.add(img.toString());
                }
            } else if (imagesObj instanceof String[]) {
                images = Arrays.asList((String[]) imagesObj);
            } else if (imagesObj instanceof String) {
                images.add((String) imagesObj);
            }
            if (!images.isEmpty()) {
                post.setImageUrl(String.join(",", images));
            } else {
                post.setImageUrl(null);
            }

            String tag = (String) postData.get("tag");
            post.setTag(tag);
            post.setUser(user);
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);

            // 清理相关Redis缓存
            Set<String> keys = redisTemplate.keys("posts:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("id", post.getPostId());
            result.put("content", post.getContent());
            result.put("images", images);
            result.put("tag", getTagInfo(tag));
            result.put("user", getUserInfo(user.getUserId()));
            result.put("likes", 0);
            result.put("comments", new ArrayList<>());
            result.put("time", post.getCreatedAt());
            return result;
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }
    @Override
    public Map<String, Object> getPosts(int page, int pageSize, String tag) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Post> pagePosts;
        long total;
        if (tag != null && !tag.isEmpty()) {
            pagePosts = postRepository.findByTagOrderByCreatedAtDesc(tag, pageable);
            total = postRepository.findByTag(tag).size();
        } else {
            pagePosts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
            total = postRepository.count();
        };

        List<Map<String, Object>> posts = new ArrayList<>();
        for (Post post : pagePosts) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", post.getPostId());
            map.put("content", post.getContent());

            // 直接返回完整图片URL
            List<String> images = new ArrayList<>();
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                String[] arr = post.getImageUrl().split(",");
                images.addAll(Arrays.asList(arr));
            }
            map.put("images", images);

            map.put("tag", getTagInfo(post.getTag()));
            map.put("user", getUserInfo(post.getUser().getUserId()));
            map.put("likes", likesRepository.countByTargetTypeAndTargetId("post", post.getPostId()));
            map.put("isLiked", false); // 如需判断当前用户是否点赞，需传userId
            map.put("comments", getComments(post.getPostId()));
            map.put("time", post.getCreatedAt());
            posts.add(map);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("total", total);
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> likePost(Integer postId, Integer userId) {
        if (userId == null) throw new IllegalArgumentException("userId 不能为空");
        boolean liked = likesRepository.existsByUserIdAndTargetTypeAndTargetId(userId, "post", postId);
        if (liked) {
            likesRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, "post", postId);
        } else {
            Like like = new Like();
            like.setUserId(userId);
            like.setTargetType("post");
            like.setTargetId(postId);
            like.setCreatedAt(LocalDateTime.now());
            likesRepository.save(like);
        }
        long likes = likesRepository.countByTargetTypeAndTargetId("post", postId);
        return Map.of("likes", likes, "isLiked", !liked);
    }

    @Override
    public Map<String, Object> commentPost(Integer postId, String content, Integer userId) {
        if (userId == null) throw new IllegalArgumentException("userId 不能为空");
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUserId(userId);
        commentRepository.save(comment);

        Map<String, Object> result = new HashMap<>();
        result.put("id", comment.getCommentId());
        result.put("content", comment.getContent());
        result.put("user", getUserInfo(userId));
        result.put("time", comment.getCreatedAt());
        return result;
    }

    // 用户信息补全
    private Map<String, Object> getUserInfo(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return Map.of();
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getUsername());
        userDetailsRepository.findById(userId).ifPresent(details -> {
            map.put("avatar", details.getAvatar());
            map.put("college", details.getCollege());
            map.put("major", details.getMajor());
        });
        return map;
    }

    private Map<String, Object> getTagInfo(String tag) {
        if ("campus".equals(tag)) return Map.of("value", "campus", "label", "校园生活", "type", "success");
        if ("club".equals(tag)) return Map.of("value", "club", "label", "社团活动", "type", "warning");
        if ("study".equals(tag)) return Map.of("value", "study", "label", "学习交流", "type", "info");
        if ("market".equals(tag)) return Map.of("value", "market", "label", "二手市场", "type", "danger");
        return Map.of("value", tag, "label", tag, "type", "info");
    }

    private List<Map<String, Object>> getComments(Integer postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Comment c : comments) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getCommentId());
            map.put("content", c.getContent());
            map.put("user", getUserInfo(c.getUserId()));
            map.put("time", c.getCreatedAt());
            result.add(map);
        }
        return result;
    }
}