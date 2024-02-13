package com.project.carparking.controller;

import com.project.carparking.config.AppConstants;
import com.project.carparking.dto.VehicleEntryExitStampResponse;
import com.project.carparking.dto.WithPaginationResponse;
import com.project.carparking.entity.VehicleEntryExitStamp;
import com.project.carparking.service.VehicleEntryExitStampService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/vehicle-entry-exit-stamps")
public class VehicleEntryExitStampController {

    @Autowired
    private VehicleEntryExitStampService vehicleEntryExitStampService;

    @GetMapping("/{stampId}")
    public ResponseEntity<VehicleEntryExitStamp> getVehicleEntryExitStampById(@PathVariable Long stampId) {
        VehicleEntryExitStamp stamp = vehicleEntryExitStampService.getVehicleEntryExitStampById(stampId);
        return ResponseEntity.ok(stamp);
    }

    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<WithPaginationResponse<VehicleEntryExitStampResponse>> findByVehicleIdAndEntryTimeBetween(
            @PathVariable Long vehicleId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {

        WithPaginationResponse<VehicleEntryExitStampResponse> response = vehicleEntryExitStampService.findByVehicleIdAndEntryTimeBetween(vehicleId, pageNo, pageSize);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleEntryExitStamp> createVehicleEntryExitStamp(@PathVariable Long vehicleId, @Valid @RequestBody VehicleEntryExitStamp vehicleEntryExitStamp) {
        VehicleEntryExitStamp createdStamp = vehicleEntryExitStampService.saveVehicleEntryExitStamp(vehicleId, vehicleEntryExitStamp);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStamp);
    }

    @PostMapping("/parking-slot/{slotNumber}")
    public ResponseEntity<VehicleEntryExitStamp> createVehicleEntryExitStampBySlotNumber(@PathVariable String slotNumber, @Valid @RequestBody VehicleEntryExitStamp vehicleEntryExitStamp) {
        VehicleEntryExitStamp createdStamp = vehicleEntryExitStampService.saveVehicleEntryExitStampBySlotNumber(slotNumber, vehicleEntryExitStamp);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStamp);
    }


    @PutMapping("/{stampId}")
    public ResponseEntity<VehicleEntryExitStamp> updateVehicleEntryExitStamp(@PathVariable Long stampId, @Valid @RequestBody VehicleEntryExitStamp updatedStamp) {
        VehicleEntryExitStamp updatedStampData = vehicleEntryExitStampService.updateVehicleEntryExitStamp(stampId, updatedStamp);
        return ResponseEntity.ok(updatedStampData);
    }

    @DeleteMapping("/{stampId}")
    public ResponseEntity<Void> deleteVehicleEntryExitStamp(@PathVariable Long stampId) {
        vehicleEntryExitStampService.deleteVehicleEntryExitStamp(stampId);
        return ResponseEntity.noContent().build();
    }
}
