package com.truckhelper.application.dtos;

import java.util.List;

public record NotificationsDto(
        List<NotificationDto> notifications
) {
}
