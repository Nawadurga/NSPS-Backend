package com.project.carparking.config;

import com.project.carparking.service.FileMetadataService;
import com.project.carparking.service.PushNotificationService;
import com.project.carparking.service.VehicleEntryExitStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private VehicleEntryExitStampService vehicleEntryExitStampService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private FileMetadataService fileMetadataService;



    @Scheduled(cron = "0 0 0 * * ?") // Run every day at 12 am
    public void deleteEntriesOlderThanOneMonth() {
        vehicleEntryExitStampService.deleteEntriesOlderThanOneMonth();
        vehicleEntryExitStampService.deleteEntriesOlderThanOneMonth();
        fileMetadataService.deleteImagesOlderThanOneMonth();

    }





}
