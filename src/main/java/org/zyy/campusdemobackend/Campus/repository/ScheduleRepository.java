package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zyy.campusdemobackend.Campus.model.Schedule;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    // 根据 scheduleId 和 userId 查询课程
    Schedule findByScheduleIdAndUserId(Integer scheduleId, Long userId);

    // 根据 userId 查询课程表，并按 day 和 period 排序
    List<Schedule> findByUserIdOrderByDayAscPeriodAsc(Long userId);

    // 根据 userId 删除课程表
    void deleteByUserId(Long userId);
}