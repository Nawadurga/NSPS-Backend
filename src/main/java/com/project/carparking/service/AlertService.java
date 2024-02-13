package com.project.carparking.service;


import com.project.carparking.dto.PushNotification;
import com.project.carparking.entity.EnumRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    @Autowired
    private  PushNotificationService pushNotificationService;

    public void processAlert(String title, String subtitle) {

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setBody(subtitle);

        pushNotificationService.sendNotificationToAdmin(pushNotification);
    }
}