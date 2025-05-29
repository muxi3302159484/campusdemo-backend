
package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.model.Message;
import org.zyy.campusdemobackend.Campus.repository.UserRepository;
import org.zyy.campusdemobackend.Campus.repository.MessageRepository;
import org.zyy.campusdemobackend.Campus.service.JwtService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtService jwtService;
    // 发送私信
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestHeader("Authorization") String token,
                                         @RequestBody Map<String, Object> payload) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User sender = userRepository.findByUsername(username);
        Integer toUserId = (Integer) payload.get("toUserId");
        String content = (String) payload.get("content");
        if (sender == null || toUserId == null || content == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "参数错误"));
        }
        Message message = new Message();
        message.setSenderId(sender.getUserId());
        message.setReceiverId(toUserId);
        message.setContent(content);
        message.setSentAt(java.time.LocalDateTime.now());
        messageRepository.save(message);
        return ResponseEntity.ok(Map.of("message", "发送成功"));
    }

    // 获取与某用户的私信记录
    @GetMapping("/messages/{userId}")
    public ResponseEntity<?> getMessages(@RequestHeader("Authorization") String token,
                                         @PathVariable Integer userId) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户未找到"));
        }
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                currentUser.getUserId(), userId, currentUser.getUserId(), userId
        );
        return ResponseEntity.ok(Map.of("messages", messages));
    }
}