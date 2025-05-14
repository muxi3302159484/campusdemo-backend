// src/main/java/org/zyy/campusdemobackend/Campus/controller/ChatController.java
package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.Message;
import org.zyy.campusdemobackend.Campus.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private MessageRepository messageRepository;

    // 获取历史消息
    // 获取历史消息
    @GetMapping("/{id}/messages")
    public List<Message> getMessages(@PathVariable Integer id, @RequestParam Integer userId) {
        // 查询双方的消息
        return messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(userId, id, id, userId);
    }

    // 发送消息
    @PostMapping("/{id}/messages")
    public Message sendMessage(@PathVariable Integer id, @RequestBody Message message) {
        message.setReceiverId(id);
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);
        return messageRepository.save(message);
    }
}