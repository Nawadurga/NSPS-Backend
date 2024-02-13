package com.project.carparking.service;


import com.project.carparking.dto.VehicleEntryExitStampResponse;
import com.project.carparking.dto.WithPaginationResponse;
import com.project.carparking.dto.converter.Converter;
import com.project.carparking.entity.ParkingSlot;
import com.project.carparking.entity.Vehicle;
import com.project.carparking.entity.VehicleEntryExitStamp;
import com.project.carparking.exception.ResourceNotFoundException;
import com.project.carparking.repository.ParkingSlotRepository;
import com.project.carparking.repository.VehicleEntryExitStampRepository;
import com.project.carparking.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleEntryExitStampService {

    @Autowired
    private VehicleEntryExitStampRepository vehicleEntryExitStampRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    public VehicleEntryExitStamp getVehicleEntryExitStampById(Long stampId) {
        return vehicleEntryExitStampRepository.findById(stampId)
                .orElseThrow(() -> new ResourceNotFoundException("Stamp with id " + stampId + " not found"));
    }



    public WithPaginationResponse<VehicleEntryExitStampResponse> findByVehicleIdAndEntryTimeBetween(Long vehicleId, int pageNo, int pageSize) {
        // Calculate start date as 30 days before the current date
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);

        // Create Pageable with sorting by entry time in descending order
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("entryTime").descending());

        // Fetch data from repository
        Page<VehicleEntryExitStamp> stampPage = vehicleEntryExitStampRepository.findByVehicleIdAndEntryTimeBetween(vehicleId, startDate, endDate, pageable);

        // Map the fetched data to response objects
        List<VehicleEntryExitStampResponse> stampResponseList = stampPage.getContent().stream()
                .map(Converter::convertToVehicleEntryExitStampResponse)
                .collect(Collectors.toList());

        // Create pagination response
        WithPaginationResponse<VehicleEntryExitStampResponse> stampAndPagination = new WithPaginationResponse<>();
        stampAndPagination.setContent(stampResponseList);
        stampAndPagination.setPaginationResponse(Converter.convertPageToPageResponse(stampPage));

        return stampAndPagination;
    }


    public VehicleEntryExitStamp saveVehicleEntryExitStamp(Long vehicleId, VehicleEntryExitStamp vehicleEntryExitStamp) {
        // Retrieve the vehicle by its ID
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with id " + vehicleId + " not found"));

        // Set the vehicle for the entry/exit stamp
        vehicleEntryExitStamp.setVehicle(vehicle);

        // Save the entry/exit stamp
        return vehicleEntryExitStampRepository.save(vehicleEntryExitStamp);
    }

    public VehicleEntryExitStamp saveVehicleEntryExitStampBySlotNumber(String slotNumber, VehicleEntryExitStamp vehicleEntryExitStamp) {
        ParkingSlot parkingSlot = parkingSlotRepository.findBySlotNumber(slotNumber).orElseThrow(() -> {
            return new ResourceNotFoundException("Parking Slot " + slotNumber + " not found");
        });
        ;

        // Retrieve the vehicle by its ID
        Vehicle vehicle = parkingSlot.getVehicle();
        // Set the vehicle for the entry/exit stamp
        vehicleEntryExitStamp.setVehicle(vehicle);

        // Save the entry/exit stamp
        return vehicleEntryExitStampRepository.save(vehicleEntryExitStamp);
    }

    public VehicleEntryExitStamp updateVehicleEntryExitStamp(Long stampId, VehicleEntryExitStamp updatedStamp) {
        // Check if the stampId is valid
        VehicleEntryExitStamp existingStamp = getVehicleEntryExitStampById(stampId);


        // Update existing stamp fields with fields from updatedStamp
        if (updatedStamp.getEntryTime() != null) {
            existingStamp.setEntryTime(updatedStamp.getEntryTime());
        }
        if (updatedStamp.getExitTime() != null) {
            existingStamp.setExitTime(updatedStamp.getExitTime());
        }

        // Save the updated stamp
        return vehicleEntryExitStampRepository.save(existingStamp);

    }




    public void deleteVehicleEntryExitStamp(Long stampId) {
        boolean existsById = vehicleEntryExitStampRepository.existsById(stampId);
        if (existsById) {
            vehicleEntryExitStampRepository.deleteById(stampId);
        } else {
            throw new ResourceNotFoundException("Stamp with id " + stampId + " not found");
        }
    }


    @Transactional
    public void deleteEntriesOlderThanOneMonth() {
        // Calculate the date one month ago
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // Delete entries older than one month
        vehicleEntryExitStampRepository.deleteByEntryTimeBefore(oneMonthAgo);
    }
}