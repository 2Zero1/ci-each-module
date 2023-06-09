package com.truckhelper.core.models;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;

@Entity
@Table(name = "notifications")
public class Notification {
    @EmbeddedId
    private NotificationId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    private String title;

    private String contents;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Notification() {
    }

    @Builder
    public Notification(
            NotificationId id,
            UserId userId,
            String title,
            String contents) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }

    public static Notification fake() {
        return Notification.builder()
                .title("NOTIFICATION-TITLE")
                .contents("NOTIFICATION-CONTENTS")
                .build();
    }
}
