package com.truckhelper.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truckhelper.core.models.Notification;
import com.truckhelper.core.models.NotificationId;
import com.truckhelper.core.models.UserId;


public interface NotificationRepository
        extends JpaRepository<Notification, NotificationId> {
    List<Notification> findAllByUserId(UserId userId);
}
