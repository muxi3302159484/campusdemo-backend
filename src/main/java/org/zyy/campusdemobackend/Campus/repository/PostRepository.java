package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zyy.campusdemobackend.Campus.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}