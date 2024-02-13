package com.project.carparking.repository;

import com.project.carparking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByNumberPlate(String numberPlate);

    List<Vehicle> findByUserId(Long userId);

    @Query("SELECT v.numberPlate FROM Vehicle v")
    String[] findAllNumberPlates();


}