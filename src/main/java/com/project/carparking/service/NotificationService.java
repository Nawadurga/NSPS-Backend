package com.project.carparking.service;

import com.project.carparking.dto.NotificationResponse;
import com.project.carparking.dto.converter.Converter;
import com.project.carparking.entity.Notification;
import com.project.carparking.entity.NotificationToken;
import com.project.carparking.entity.User;
import com.project.carparking.exception.ResourceNotFoundException;
import com.project.carparking.repository.NotificationRepository;
import com.project.carparking.repository.NotificationTokenRepository;
import com.project.carparking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationTokenRepository notificationTokenRepository;


    // Method to fetch notifications by userId
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByDateDesc(userId);
        return convertToResponseList(notifications);
    }


    private List<NotificationResponse> convertToResponseList(List<Notification> notifications) {
        List<NotificationResponse> responseList = new ArrayList<>();
        for (Notification notification : notifications) {
            responseList.add(Converter.convertToNotificationResponse(notification));
        }
        return responseList;
    }

    public Notification postNotification(Long userId, Notification notification) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        notification.setUser(user);
        return notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotificationOlderThanOneMonth() {
        // Calculate the date one month ago
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // Delete entries older than one month
        notificationRepository.deleteByDateBefore(oneMonthAgo);
    }

    @Transactional
    public void deleteByUserId(Long userId) {

        // Delete entries older than one month
        notificationRepository.deleteByUserId(userId);
    }
}
