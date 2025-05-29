package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zyy.campusdemobackend.Campus.model.Friendship;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    List<Friendship> findByUser1IdOrUser2IdAndStatus(Integer user1Id, Integer user2Id, String status);
    // 查询某用户所有已同意的好友关系（双向）
    @Query("SELECT f FROM Friendship f WHERE (f.user1Id = :userId OR f.user2Id = :userId) AND f.status = 'accepted'")
    List<Friendship> findAcceptedFriendships(@Param("userId") Integer userId);

    // 判断是否存在某一方向的好友关系
    boolean existsByUser1IdAndUser2Id(Integer user1Id, Integer user2Id);

    // 判断是否存在双向好友关系
    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE (f.user1Id = :user1 AND f.user2Id = :user2) OR (f.user1Id = :user2 AND f.user2Id = :user1)")
    boolean existsFriendshipBetween(@Param("user1") Integer user1, @Param("user2") Integer user2);
}