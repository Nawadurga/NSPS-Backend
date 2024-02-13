package com.project.carparking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public  class PushNotification {
    private List<String> to;
    private  String title;
    private  String body;
    private  String data;

}