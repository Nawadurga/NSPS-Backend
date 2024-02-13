package com.project.carparking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminPushNotificationData {

    private List<Long> adminUserIds;
    private List<String> pushNotificationTokens;
}
