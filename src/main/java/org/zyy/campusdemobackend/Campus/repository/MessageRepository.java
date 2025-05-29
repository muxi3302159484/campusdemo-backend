package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zyy.campusdemobackend.Campus.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            Integer senderId1, Integer receiverId1, Integer receiverId2, Integer senderId2
    );
}