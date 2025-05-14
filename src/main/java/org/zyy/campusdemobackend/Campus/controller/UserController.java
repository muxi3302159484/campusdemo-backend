package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.model.Schedule;
import org.zyy.campusdemobackend.Campus.model.Notification;

import org.zyy.campusdemobackend.Campus.repository.UserRepository;
import org.zyy.campusdemobackend.Campus.repository.ScheduleRepository;
import org.zyy.campusdemobackend.Campus.repository.NotificationRepository;

import org.zyy.campusdemobackend.Campus.service.JwtService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtService jwtService;

    // 获取用户信息
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "用户未找到"));
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getUserId(),
                "name", user.getUsername(),
                "role", user.getRole(),
                "email", user.getSchoolEmail(),
                "avatar", user.getAvatar()
        ));
    }

    // 更新用户信息
    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> updateData) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "用户未找到"));
        }
        if (updateData.containsKey("avatar")) user.setAvatar((String) updateData.get("avatar"));
        if (updateData.containsKey("email")) user.setSchoolEmail((String) updateData.get("email"));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "用户信息更新成功"));
    }

    // 获取通知列表
    @GetMapping("/notifications")
    public ResponseEntity<?> getUserNotifications(@RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        if (notifications == null || notifications.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(notifications);
    }

    // 标记通知为已读
    @PostMapping("/notifications/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "通知未找到"));
        }
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok(Map.of("message", "通知已标记为已读"));
    }

    // 获取课程表
    @GetMapping("/schedule")
    public ResponseEntity<?> getSchedule(@RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        List<Schedule> schedules = scheduleRepository.findByUserIdOrderByDayAscPeriodAsc(userId);
        return ResponseEntity.ok(schedules);
    }

    // 更新课程表
    @PutMapping("/schedule")
    public ResponseEntity<?> updateSchedule(@RequestHeader("Authorization") String token, @RequestBody List<Schedule> updatedSchedules) {
        Long userId = getCurrentUserId(token);
        // 删除旧课程表
        scheduleRepository.deleteByUserId(userId);
        // 保存新课程表
        for (Schedule updatedSchedule : updatedSchedules) {
            updatedSchedule.setUserId(userId);
            scheduleRepository.save(updatedSchedule);
        }
        return ResponseEntity.ok(Map.of("message", "课程表更新成功"));
    }

    // 获取当前用户ID（建议从token解析，示例实现）
    private Long getCurrentUserId(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);
        return user != null ? user.getUserId().longValue() : 1L;
    }
}
