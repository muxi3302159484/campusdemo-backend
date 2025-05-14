package org.zyy.campusdemobackend.Campus.model;

import org.springframework.stereotype.Service;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.model.Schedule;
import org.zyy.campusdemobackend.Campus.model.Notification;

import java.util.List;

@Service
public class UserService {

    public User getCurrentUserInfo() {
        // 实现获取当前用户信息的逻辑
        return new User();
    }

    public List<Schedule> getCurrentUserSchedule() {
        // 实现获取当前用户日程的逻辑
        return List.of();
    }

    public List<Notification> getCurrentUserNotifications() {
        // 实现获取当前用户通知的逻辑
        return List.of();
    }

    public void markNotificationAsRead(Long id) {
        // 实现标记通知为已读的逻辑
    }
}