package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zyy.campusdemobackend.Campus.model.Like;

public interface LikesRepository extends JpaRepository<Like, Integer> {
    boolean existsByUserIdAndTargetTypeAndTargetId(Integer userId, String targetType, Integer targetId);
    void deleteByUserIdAndTargetTypeAndTargetId(Integer userId, String targetType, Integer targetId);
    long countByTargetTypeAndTargetId(String targetType, Integer targetId);
}