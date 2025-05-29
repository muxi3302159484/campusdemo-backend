package org.zyy.campusdemobackend.Campus.service;

import java.util.Map;

public interface PostService {
    Map<String, Object> createPost(Map<String, Object> postData);
    Map<String, Object> getPosts(int page, int pageSize, String tag);
    Map<String, Object> likePost(Integer postId, Integer userId); // 确保有 userId 参数
    Map<String, Object> commentPost(Integer postId, String content, Integer userId);
}