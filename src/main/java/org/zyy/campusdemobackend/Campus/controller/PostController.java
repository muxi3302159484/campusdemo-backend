package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.Post;
import org.zyy.campusdemobackend.Campus.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    // 发布动态
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    // 获取动态列表（分页）
    @GetMapping
    public List<Post> getPosts(@RequestParam int page, @RequestParam int pageSize) {
        return postRepository.findAll().stream()
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .toList();
    }

    // 点赞动态（示例：假设点赞数存储在其他表中）
    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable Integer postId) {
        // 点赞逻辑（需要根据实际需求实现）
        return "点赞成功";
    }
}