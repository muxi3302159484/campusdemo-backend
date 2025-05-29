// FriendshipService.java
package org.zyy.campusdemobackend.Campus.service;

import org.zyy.campusdemobackend.Campus.model.User;
import java.util.List;
import java.util.Map;

public interface FriendshipService {
    List<Map<String, Object>> searchUsers(String keyword);
    Map<String, Object> addFriend(String token, Map<String, Object> payload);
    void sendFriendRequest(Integer fromUserId, Integer toUserId);
    void acceptFriendRequest(Integer friendshipId, Integer actionUserId);
    void rejectFriendRequest(Integer friendshipId, Integer actionUserId);
    List<User> getFriends(Integer userId);
}