package com.project.carparking.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Length(min = 3, max = 25, message = "Name should be between 3 to 25 characters.")
    private String name;

    @Length(min = 10, max = 10, message = "Invalid phone number")
    @Pattern(regexp = "^9\\d*", message = "Invalid phone number")
    private String phoneNo;

    @Length(min = 6, message = "Password must be grater than 6 character")
    private String password;


}