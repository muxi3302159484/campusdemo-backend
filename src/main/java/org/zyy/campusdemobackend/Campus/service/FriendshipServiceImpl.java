package org.zyy.campusdemobackend.Campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zyy.campusdemobackend.Campus.model.Friendship;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.repository.FriendshipRepository;
import org.zyy.campusdemobackend.Campus.repository.UserDetailsRepository;
import org.zyy.campusdemobackend.Campus.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Map<String, Object>> searchUsers(String keyword) {
        List<User> users = userRepository.findByUsernameContaining(keyword);
        return users.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getUserId());
            map.put("name", user.getUsername());
            // 查询 UserDetails 获取 avatar
            String avatar = userDetailsRepository.findById(user.getUserId())
                    .map(details -> details.getAvatar())
                    .orElse("1");
            map.put("avatar", avatar);
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> addFriend(String token, Map<String, Object> payload) {
        Integer fromUserId = getCurrentUserId(token);
        Object userIdObj = payload.get("userId");
        if (userIdObj == null) return Map.of("error", "userId不能为空");
        Integer toUserId;
        try {
            toUserId = Integer.valueOf(userIdObj.toString());
        } catch (Exception e) {
            return Map.of("error", "userId格式错误");
        }
        try {
            sendFriendRequest(fromUserId, toUserId);
        } catch (IllegalArgumentException e) {
            return Map.of("error", e.getMessage());
        }
        return Map.of("message", "好友请求已发送");
    }

    @Override
    public void sendFriendRequest(Integer fromUserId, Integer toUserId) {
        if (fromUserId.equals(toUserId)) throw new IllegalArgumentException("不能添加自己为好友");
        if (!userRepository.existsById(toUserId)) throw new IllegalArgumentException("目标用户不存在");
        if (!userRepository.existsById(fromUserId)) throw new IllegalArgumentException("当前用户不存在");
        boolean exists = friendshipRepository.existsFriendshipBetween(fromUserId, toUserId);
        if (exists) throw new IllegalArgumentException("好友请求已存在或已是好友");
        Friendship friendship = new Friendship();
        friendship.setUser1Id(fromUserId);
        friendship.setUser2Id(toUserId);
        friendship.setStatus("pending");
        friendship.setActionUserId(fromUserId);
        friendship.setCreatedAt(LocalDateTime.now());
        friendship.setUpdatedAt(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    @Override
    public void acceptFriendRequest(Integer friendshipId, Integer actionUserId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("请求不存在"));
        if (!friendship.getUser2Id().equals(actionUserId)) {
            throw new IllegalArgumentException("无权操作该请求");
        }
        if (!"pending".equals(friendship.getStatus())) {
            throw new IllegalArgumentException("该请求已处理");
        }
        friendship.setStatus("accepted");
        friendship.setActionUserId(actionUserId);
        friendship.setUpdatedAt(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    @Override
    public void rejectFriendRequest(Integer friendshipId, Integer actionUserId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("请求不存在"));
        if (!friendship.getUser2Id().equals(actionUserId)) {
            throw new IllegalArgumentException("无权操作该请求");
        }
        if (!"pending".equals(friendship.getStatus())) {
            throw new IllegalArgumentException("该请求已处理");
        }
        friendship.setStatus("rejected");
        friendship.setActionUserId(actionUserId);
        friendship.setUpdatedAt(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    @Override
    public List<User> getFriends(Integer userId) {
        List<Friendship> friendships = friendshipRepository.findAcceptedFriendships(userId);
        List<User> friends = new ArrayList<>();
        for (Friendship f : friendships) {
            Integer friendId = f.getUser1Id().equals(userId) ? f.getUser2Id() : f.getUser1Id();
            userRepository.findById(friendId).ifPresent(friends::add);
        }
        return friends;
    }

    // TODO: 根据你的 JWT 解析逻辑实现
    private Integer getCurrentUserId(String token) {
        // 示例：实际应解析 token 获取 userId
        return 1;
    }
}