package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.model.Friendship;
import org.zyy.campusdemobackend.Campus.repository.UserDetailsRepository;
import org.zyy.campusdemobackend.Campus.repository.UserRepository;
import org.zyy.campusdemobackend.Campus.repository.FriendshipRepository;
import org.zyy.campusdemobackend.Campus.service.FriendshipService;
import org.zyy.campusdemobackend.Campus.service.JwtService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired

    private FriendshipService friendshipService;

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String keyword) {
        List<User> users = userRepository.findByUsernameContaining(keyword);
        List<Map<String, Object>> result = users.stream().map(user -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("userId", user.getUserId());
            map.put("name", user.getUsername());
            // 查询 UserDetails 获取 avatar
            String avatar = userDetailsRepository.findById(user.getUserId())
                    .map(details -> details.getAvatar())
                    .orElse("1");
            map.put("avatar", avatar);
            return map;
        }).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(Map.of("users", result));
    }

    // 发送好友请求
    @PostMapping("/addfriend")
    public ResponseEntity<?> addFriend(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload) {
        Long currentUserId = getCurrentUserId(token);
        Object userIdObj = payload.get("userId");
        if (userIdObj == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "userId不能为空"));
        }
        Integer friendUserId;
        try {
            friendUserId = Integer.valueOf(userIdObj.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "userId格式错误"));
        }
        if (currentUserId.intValue() == friendUserId) {
            return ResponseEntity.badRequest().body(Map.of("message", "不能添加自己为好友"));
        }
        if (friendshipRepository.existsByUser1IdAndUser2Id(currentUserId.intValue(), friendUserId) ||
                friendshipRepository.existsByUser1IdAndUser2Id(friendUserId, currentUserId.intValue())) {
            return ResponseEntity.badRequest().body(Map.of("message", "好友请求已存在或已是好友"));
        }
        Friendship friendship = new Friendship();
        friendship.setUser1Id(currentUserId.intValue());
        friendship.setUser2Id(friendUserId);
        friendship.setStatus("pending");
        friendship.setActionUserId(currentUserId.intValue());
        friendship.setCreatedAt(java.time.LocalDateTime.now());
        friendship.setUpdatedAt(java.time.LocalDateTime.now());
        friendshipRepository.save(friendship);
        return ResponseEntity.ok(Map.of("message", "好友请求已发送"));
    }

    // 获取当前用户ID
    private Long getCurrentUserId(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);
        return user != null ? user.getUserId().longValue() : 1L;
    }

    // 同意好友请求
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody Map<String, Integer> payload) {
        friendshipService.acceptFriendRequest(payload.get("friendshipId"), payload.get("actionUserId"));
        return ResponseEntity.ok(Map.of("message", "已同意好友请求"));
    }

    // 拒绝好友请求
    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestBody Map<String, Integer> payload) {
        friendshipService.rejectFriendRequest(payload.get("friendshipId"), payload.get("actionUserId"));
        return ResponseEntity.ok(Map.of("message", "已拒绝好友请求"));
    }

    // 查询已同意的联系人
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getFriends(@PathVariable Integer userId) {
        List<User> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(Map.of("friends", friends));
    }
}