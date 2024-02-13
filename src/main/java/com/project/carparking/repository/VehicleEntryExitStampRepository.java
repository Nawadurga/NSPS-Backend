package com.project.carparking.repository;

import com.project.carparking.entity.VehicleEntryExitStamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface VehicleEntryExitStampRepository extends JpaRepository<VehicleEntryExitStamp, Long> {
    Page<VehicleEntryExitStamp> findByVehicleIdAndEntryTimeBetween(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    void deleteByEntryTimeBefore(LocalDateTime date);

    Optional<VehicleEntryExitStamp> findFirstByOrderByEntryTimeDesc();
    Optional<VehicleEntryExitStamp> findFirstByVehicleIdOrderByEntryTimeDesc(Long vehicleId);
}