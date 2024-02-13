package com.project.carparking.repository;

import com.project.carparking.entity.EnumRole;
import com.project.carparking.entity.ParkingSlot;
import com.project.carparking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    Optional<ParkingSlot>  findBySlotNumber(String slotNumber);

    Boolean existsBySlotNumber(String slotNumber);

}