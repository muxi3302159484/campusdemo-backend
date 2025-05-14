package org.zyy.campusdemobackend.Campus.repository;

import java.util.List;
import org.zyy.campusdemobackend.Campus.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            Integer senderId1, Integer receiverId1, Integer senderId2, Integer receiverId2
    );

    List<Message> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);
}