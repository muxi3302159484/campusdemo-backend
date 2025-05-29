package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.zyy.campusdemobackend.Campus.repository.*;

import org.zyy.campusdemobackend.Campus.service.JwtService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired

    private UserDetailsRepository userDetailsRepository;
    //info返回数据
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "用户未找到"));
        }

        UserDetails details = userDetailsRepository.findById(user.getUserId()).orElse(null);

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("userDetails", details);

        return ResponseEntity.ok(result);
    }

    // 更新用户信息时，更新 UserDetails 的 avatar
    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> updateData) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "用户未找到"));
        }
        if (updateData.containsKey("email")) user.setSchoolEmail((String) updateData.get("email"));
        userRepository.save(user);

        if (updateData.containsKey("avatar")) {
            UserDetails userDetails = userDetailsRepository.findById(user.getUserId()).orElse(null);
            if (userDetails != null) {
                userDetails.setAvatar((String) updateData.get("avatar"));
                userDetailsRepository.save(userDetails);
            }
        }
        return ResponseEntity.ok(Map.of("message", "用户信息更新成功"));
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
    //用户通知
    @GetMapping("/notifications")
    public List<Map<String, Object>> getNotifications(@RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        List<Map<String, Object>> notifications = new ArrayList<>();

        // 1. 当天课程提醒
        String today = LocalDate.now().getDayOfWeek().name();
        List<Schedule> todayCourses = scheduleRepository.findByUserIdOrderByDayAscPeriodAsc(userId);
        for (Schedule s : todayCourses) {
            if (today.equalsIgnoreCase(s.getDay())) {
                Map<String, Object> n = new HashMap<>();
                n.put("type", "schedule");
                n.put("title", "今日课程提醒");
                n.put("content", s.getCourseName() + " @ " + s.getLocation() + " 第" + s.getPeriod() + "节");
                n.put("time", LocalDateTime.now());
                notifications.add(n);
            }
        }

        // 2. 重大活动提前一周提醒
        LocalDate now = LocalDate.now();
        List<Event> events = eventRepository.findAll();
        for (Event e : events) {
            if (e.getStartTime() != null) {
                LocalDate start = e.getStartTime().toLocalDate();
                if (!start.isBefore(now) && !start.isAfter(now.plusDays(7))) {
                    Map<String, Object> n = new HashMap<>();
                    n.put("type", "event");
                    n.put("title", "活动预告");
                    n.put("content", e.getEventName() + " 将于 " + e.getStartTime() + " 举行");
                    n.put("time", LocalDateTime.now());
                    notifications.add(n);
                }
            }
        }

        // 3. 贴文被点赞/评论通知（不通知自己）
        List<Post> myPosts = postRepository.findByUser_UserId(userId.intValue());
        for (Post post : myPosts) {
            // 点赞
            List<Like> likes = likesRepository.findAll();
            for (Like like : likes) {
                if ("post".equals(like.getTargetType()) && post.getPostId().equals(like.getTargetId())
                        && !like.getUserId().equals(userId.intValue())) {
                    Map<String, Object> n = new HashMap<>();
                    n.put("type", "like");
                    n.put("title", "你的贴文被点赞");
                    n.put("content", "用户 " + like.getUserId() + " 点赞了你的贴文");
                    n.put("time", like.getCreatedAt());
                    notifications.add(n);
                }
            }
            // 评论
            List<Comment> comments = commentRepository.findByPostId(post.getPostId());
            for (Comment c : comments) {
                if (!c.getUserId().equals(userId.intValue())) {
                    Map<String, Object> n = new HashMap<>();
                    n.put("type", "comment");
                    n.put("title", "你的贴文有新评论");
                    n.put("content", "用户 " + c.getUserId() + " 评论了你的贴文: " + c.getContent());
                    n.put("time", c.getCreatedAt());
                    notifications.add(n);
                }
            }
        }

        // 按时间倒序
        notifications.sort((a, b) -> ((LocalDateTime) b.get("time")).compareTo((LocalDateTime) a.get("time")));
        return notifications;
    }

    // 获取当前用户ID（
    private Long getCurrentUserId(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);
        return user != null ? user.getUserId().longValue() : 1L;
    }

}