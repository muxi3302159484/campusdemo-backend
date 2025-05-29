package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zyy.campusdemobackend.Campus.model.Post;
import java.util.List;
//支持按标签和时间倒序分页查询
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByTag(String tag);

    List<Post> findByTagOrderByCreatedAtDesc(String tag, org.springframework.data.domain.Pageable pageable);

    List<Post> findByUser_UserId(Integer userId);

    List<Post> findAllByOrderByCreatedAtDesc(org.springframework.data.domain.Pageable pageable);
}