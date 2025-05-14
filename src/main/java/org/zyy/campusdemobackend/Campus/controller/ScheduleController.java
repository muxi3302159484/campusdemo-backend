package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.Schedule;
import org.zyy.campusdemobackend.Campus.service.ScheduleService;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // 获取课程表
    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedule(@RequestParam Long userId) {
        List<Schedule> schedule = scheduleService.getScheduleByUserId(userId);
        return ResponseEntity.ok(schedule);
    }

    // 更新课程表
    @PutMapping
    public ResponseEntity<?> updateSchedule(@RequestBody List<Schedule> schedules, @RequestParam Long userId) {
        scheduleService.updateSchedule(userId, schedules);
        return ResponseEntity.ok("课程表更新成功");
    }
}