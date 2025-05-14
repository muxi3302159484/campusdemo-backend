package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.zyy.campusdemobackend.Campus.model.Message;
import org.zyy.campusdemobackend.Campus.repository.MessageRepository;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/chat") // 接收客户端发送的消息
    public void sendMessage(Message message) {
        // 持久化消息
        message.setSentAt(java.time.LocalDateTime.now());
        messageRepository.save(message);

        // 发送消息给指定用户
        String destination = "/queue/messages/" + message.getReceiverId();
        messagingTemplate.convertAndSend(destination, message);
    }
}