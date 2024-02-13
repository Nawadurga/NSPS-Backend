package com.project.carparking.dto;

import com.project.carparking.entity.EnumRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private  Long id;
    private  String name;
    private String phoneNo;
    private Boolean isAdmin;
    private String token;
}