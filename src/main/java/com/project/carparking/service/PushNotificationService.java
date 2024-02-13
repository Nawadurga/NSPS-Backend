package com.project.carparking.service;


import com.project.carparking.dto.AdminPushNotificationData;
import com.project.carparking.dto.NotificationScenario;
import com.project.carparking.dto.PushNotification;
import com.project.carparking.entity.EnumRole;
import com.project.carparking.entity.Notification;
import com.project.carparking.entity.User;
import com.project.carparking.exception.ResourceNotFoundException;
import com.project.carparking.repository.NotificationTokenRepository;
import com.project.carparking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PushNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificationTokenRepository notificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;


    @Value("${expo.push.notification.url}")
    private String expoPushNotificationUrl;

    public List<String> getAllPushNotificationsTokensFromUsers(Long userId) {
        return notificationTokenRepository.findPushNotificationTokensByUserId(userId);
    }


    public void sendNotificationToUser(PushNotification pushNotification, Long userId) {
        List<String> allTokens = getAllPushNotificationsTokensFromUsers(userId);
        sendNotification(pushNotification, allTokens, userId);
    }

    public void sendNotificationToAdmin(PushNotification pushNotification) {
        User user = userRepository.findFirstByRoleOrderByCreatedAtAsc(EnumRole.ADMIN).orElseThrow(() -> {
            return new ResourceNotFoundException("Admin Not Found");
        });

        sendNotificationToUser(pushNotification, user.getId());
    }

    private void sendNotification(PushNotification pushNotification, List<String> allTokens, Long userId) {
        String title = pushNotification.getTitle();
        String body = pushNotification.getBody();
        String data = "{\"screen\":\"Notification\"}";

        Notification notification = new Notification();
        notification.setTitle(title); // Fixed typo: Change setSubtitle to setTitle
        notification.setSubtitle(body);
        notification.setDate(LocalDateTime.now());
        notificationService.postNotification(userId, notification);

        int batchSize = 100;
        for (int i = 0; i < allTokens.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, allTokens.size());
            List<String> to = allTokens.subList(i, endIndex);
            PushNotification pushNotificationFinal = new PushNotification(to, title, body, data);
            sendPushNotification(pushNotificationFinal);
        }
    }



    public void sendPushNotification(PushNotification pushNotification) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PushNotification> requestEntity = new HttpEntity<>(pushNotification, headers);

        try {
            restTemplate.exchange(expoPushNotificationUrl, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            logger.error("Failed to send push notification", e);
        }
    }

    public void sendOnboardingNotification(Long userId) {
        String title = NotificationScenario.ONBOARDING.getTitle();
        String subtitle = NotificationScenario.ONBOARDING.getSubtitle();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setBody(subtitle);

        sendNotificationToUser(pushNotification, userId);
    }

    public void sendVehicleAddedNotification(Long userId) {
        String title = NotificationScenario.VEHICLE_ADDED.getTitle();
        String subtitle = NotificationScenario.VEHICLE_ADDED.getSubtitle();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setBody(subtitle);

        sendNotificationToUser(pushNotification, userId);
    }

    public void sendVehicleRemovedNotification(Long userId) {
        String title = NotificationScenario.VEHICLE_Removed.getTitle();
        String subtitle = NotificationScenario.VEHICLE_Removed.getSubtitle();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setBody(subtitle);

        sendNotificationToUser(pushNotification, userId);
    }

    public void sendParkingSpaceAllocatedNotification(Long userId, String parkingSlotNumber) {
        String title = NotificationScenario.PARKING_SPACE_ALLOCATED.getTitle();
        String subtitle = NotificationScenario.PARKING_SPACE_ALLOCATED.getSubtitle() + " Your parking slot number is " + parkingSlotNumber + " Park with ease!";


        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setBody(subtitle);

        sendNotificationToUser(pushNotification, userId);
    }



    public void sendPaymentSuccessNotification(Long userId) {
        String title = NotificationScenario.PAYMENT_SUCCESS.getTitle();
        String subtitle = NotificationScenario.PAYMENT_SUCCESS.getSubtitle();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setBody(subtitle);

        sendNotificationToUser(pushNotification, userId);
    }

}

