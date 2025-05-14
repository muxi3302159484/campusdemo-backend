package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zyy.campusdemobackend.Campus.model.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}