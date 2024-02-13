package com.project.carparking.controller;

import com.project.carparking.dto.NotificationResponse;
import com.project.carparking.entity.Notification;
import com.project.carparking.entity.NotificationToken;
import com.project.carparking.service.NotificationService;
import com.project.carparking.service.NotificationTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notifications-token")
public class NotificationTokenController {

    @Autowired
    private NotificationTokenServices notificationTokenServices;

    @PostMapping("/users/{userId}")
    public ResponseEntity<NotificationToken> findOrCreateByPushNotificationToken(@PathVariable Long userId, @RequestBody NotificationToken notificationToken) {
        NotificationToken token = notificationTokenServices.findOrCreateByPushNotificationToken(notificationToken.getPushNotificationToken(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }



}


