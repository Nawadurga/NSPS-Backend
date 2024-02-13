package com.project.carparking.dto.converter;


import com.project.carparking.dto.NotificationResponse;
import com.project.carparking.dto.PaginationResponse;
import com.project.carparking.dto.UserResponse;
import com.project.carparking.dto.VehicleEntryExitStampResponse;
import com.project.carparking.entity.EnumRole;
import com.project.carparking.entity.Notification;
import com.project.carparking.entity.User;
import com.project.carparking.entity.VehicleEntryExitStamp;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Converter {
    private static final DateTimeFormatter NOTIFICATION_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");


    public static NotificationResponse convertToNotificationResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setSubtitle(notification.getSubtitle());
        response.setDate(notification.getDate().format(NOTIFICATION_DATE_TIME_FORMATTER));
        return response;
    }


    public static UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNo(user.getPhoneNo())
                .address(user.getAddress())
                .isAdmin(user.getRole() == EnumRole.ADMIN)
                .vehicles(user.getVehicles())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static VehicleEntryExitStampResponse convertToVehicleEntryExitStampResponse(VehicleEntryExitStamp stamp) {
        VehicleEntryExitStampResponse response = new VehicleEntryExitStampResponse();
        response.setEntryDate(stamp.getEntryTime().toLocalDate());

        // Format entry time to display only hours, minutes, and seconds
        response.setEntryTime(stamp.getEntryTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        if (stamp.getExitTime() != null) {
            // Calculate total hours parked
            long hours = ChronoUnit.HOURS.between(stamp.getEntryTime(), stamp.getExitTime());
            long minutes = ChronoUnit.MINUTES.between(stamp.getEntryTime(), stamp.getExitTime()) % 60;
            double totalHoursParked = hours + (double) minutes / 60;

            // Round total hours parked to two decimal places
            totalHoursParked = Math.round(totalHoursParked * 100.0) / 100.0;

            response.setTotalHoursParked(totalHoursParked);
        } else {
            response.setTotalHoursParked(null);
        }

        return response;
    }




    public static PaginationResponse convertPageToPageResponse(Page<?> page){
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setPageNo(page.getNumber());
        paginationResponse.setPageSize(page.getSize());
        paginationResponse.setTotalElements(page.getTotalElements());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setLast(page.isLast());

        return paginationResponse;
    }


}
