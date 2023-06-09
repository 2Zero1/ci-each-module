package com.truckhelper.application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.application.dtos.NotificationDto;
import com.truckhelper.application.dtos.NotificationsDto;
import com.truckhelper.application.repositories.NotificationRepository;
import com.truckhelper.core.models.Notification;
import com.truckhelper.core.models.UserId;

@RestController
@RequestMapping("notifications")
public class NotificationController {
    private final NotificationRepository notificationRepository;

    private final ModelMapper modelMapper;

    public NotificationController(
            NotificationRepository notificationRepository,
            ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public NotificationsDto list(@RequestAttribute UserId userId) {
        List<Notification> notifications = notificationRepository
                .findAllByUserId(userId);

        return new NotificationsDto(
                notifications.stream()
                        .map(this::notificationToDto)
                        .collect(Collectors.toList())
        );
    }

    public NotificationDto notificationToDto(Notification notification) {
        return modelMapper.map(notification, NotificationDto.class);
    }
}
