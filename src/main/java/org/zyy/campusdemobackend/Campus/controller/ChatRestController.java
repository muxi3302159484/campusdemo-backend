package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.Message;
import org.zyy.campusdemobackend.Campus.repository.MessageRepository;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private MessageRepository messageRepository;

    // 获取聊天记录
    @GetMapping("/{userId}/messages")
    public List<Message> getMessages(@PathVariable Integer userId) {
        return messageRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    // 发送消息
    @PostMapping("/{receiverId}/messages")
    public Message sendMessage(@PathVariable Integer receiverId, @RequestBody Message message) {
        message.setReceiverId(receiverId);
        return messageRepository.save(message);
    }
}