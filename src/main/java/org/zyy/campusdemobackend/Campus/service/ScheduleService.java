package org.zyy.campusdemobackend.Campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zyy.campusdemobackend.Campus.model.Schedule;
import org.zyy.campusdemobackend.Campus.repository.ScheduleRepository;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    // 获取用户课程表
    public List<Schedule> getScheduleByUserId(Long userId) {
        return scheduleRepository.findByUserIdOrderByDayAscPeriodAsc(userId);
    }

    // 更新课程表
    @Transactional
    public void updateSchedule(Long userId, List<Schedule> schedules) {
        // 删除用户的旧课程表
        scheduleRepository.deleteByUserId(userId);

        // 保存新的课程表
        for (Schedule schedule : schedules) {
            schedule.setUserId(userId);
            scheduleRepository.save(schedule);
        }
    }
}