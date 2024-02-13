package com.project.carparking.controller;

import com.project.carparking.dto.NotificationResponse;
import com.project.carparking.entity.Notification;
import com.project.carparking.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private  NotificationService notificationService;



    @GetMapping("/users/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<Notification> postNotification(@PathVariable Long userId, @RequestBody Notification notification) {
        Notification savedNotification = notificationService.postNotification(userId, notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotification);
    }

    @DeleteMapping("/deleteOlderThanOneMonth")
    public ResponseEntity<?> deleteNotificationOlderThanOneMonth() {
        notificationService.deleteNotificationOlderThanOneMonth();
        return ResponseEntity.noContent().build();
    }
}


