package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class NotificationDto {
    private String title;

    private String contents;

    private String createdAt;

    private NotificationDto() {
    }
}
