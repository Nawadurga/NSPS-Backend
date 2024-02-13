package com.project.carparking.service;

import com.project.carparking.entity.NotificationToken;
import com.project.carparking.entity.User;
import com.project.carparking.exception.ResourceNotFoundException;
import com.project.carparking.repository.NotificationTokenRepository;
import com.project.carparking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationTokenServices {

    @Autowired
    NotificationTokenRepository notificationTokenRepository;

    @Autowired
    UserRepository uerRepository;


    public NotificationToken findOrCreateByPushNotificationToken(String pushNotificationToken, Long userId) {
         User user = uerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        NotificationToken existingToken = notificationTokenRepository.findByPushNotificationTokenAndUserId(pushNotificationToken, userId);
        if (existingToken != null) {
            return existingToken;
        }

        NotificationToken newToken = new NotificationToken();
        newToken.setPushNotificationToken(pushNotificationToken);
        newToken.setUser(user);

        return notificationTokenRepository.save(newToken);
    }

    @Transactional
    public void deleteByUserId(Long userId) {

        // Delete entries older than one month
        notificationTokenRepository.deleteByUserId(userId);
    }

}
