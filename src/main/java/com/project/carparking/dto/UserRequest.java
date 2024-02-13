package com.project.carparking.dto;

import com.project.carparking.entity.EnumRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRequest {
    @NotBlank(message = "Name can not be blank")
    @Length(min = 3, max = 25, message = "Name should be between 3 to 25 characters")
    private String name;


    private String address;

    @Length(min = 10, message = "Phone number must be 10 digits and valid")
    private String phoneNo;

    private EnumRole role;

    // Getters and setters
}
