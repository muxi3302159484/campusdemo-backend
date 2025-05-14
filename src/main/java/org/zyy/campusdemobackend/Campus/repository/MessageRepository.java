package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zyy.campusdemobackend.Campus.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);
}