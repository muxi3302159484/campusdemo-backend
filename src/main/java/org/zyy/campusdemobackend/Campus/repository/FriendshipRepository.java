package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zyy.campusdemobackend.Campus.model.Friendship;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    List<Friendship> findByUser1IdOrUser2Id(Integer user1Id, Integer user2Id);
}