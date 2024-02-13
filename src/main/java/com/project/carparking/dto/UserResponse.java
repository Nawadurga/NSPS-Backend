package com.project.carparking.dto;

import com.project.carparking.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private  Long id;
    private  String name;
    private String phoneNo;
    private String address;
    private Boolean isAdmin;
    private List<Vehicle> vehicles;
    private LocalDate createdAt;
}