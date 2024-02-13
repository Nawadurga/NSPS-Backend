package com.project.carparking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class ImageUrlWithCreationDate {
    private String imageUrl;
    private LocalDateTime createdAt;


    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    public String getFormattedCreatedAt() {
        return createdAt.format(DATE_TIME_FORMATTER);
    }
}