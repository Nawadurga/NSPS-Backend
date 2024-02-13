package com.project.carparking.controller;

import com.project.carparking.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/alert")
    public ResponseEntity<String> receiveAlert(@RequestParam("title") String title, @RequestParam("subtitle") String subtitle) {

        alertService.processAlert(title, subtitle);

        return ResponseEntity.ok("Alert with title and subtitle processed successfully");
    }
}