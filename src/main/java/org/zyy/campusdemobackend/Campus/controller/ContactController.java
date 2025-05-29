package org.zyy.campusdemobackend.Campus.controller;

import org.zyy.campusdemobackend.Campus.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.Friendship;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.repository.FriendshipRepository;
import org.zyy.campusdemobackend.Campus.repository.UserRepository;
import org.zyy.campusdemobackend.Campus.service.JwtService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsRepository userDetailsRepository; // 添加这一行

    @GetMapping("/contacts")
    public ResponseEntity<?> getContacts(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户未找到"));
        }
        List<Friendship> friendships = friendshipRepository.findByUser1IdOrUser2IdAndStatus(
                currentUser.getUserId(), currentUser.getUserId(), "accepted");
        Set<Integer> friendIds = new HashSet<>();
        for (Friendship f : friendships) {
            if (f.getUser1Id().equals(currentUser.getUserId())) {
                friendIds.add(f.getUser2Id());
            } else {
                friendIds.add(f.getUser1Id());
            }
        }
        List<User> friends = userRepository.findAllById(friendIds);
        List<Map<String, Object>> contacts = friends.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getUserId());
            map.put("name", user.getUsername());
            // 查询 UserDetails 获取 avatar
            String avatar = userDetailsRepository.findById(user.getUserId())
                    .map(details -> details.getAvatar())
                    .orElse("1");
            map.put("avatar", avatar);
            map.put("lastMessage", "");
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("contacts", contacts));
    }
}