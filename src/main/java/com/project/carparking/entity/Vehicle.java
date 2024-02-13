package com.project.carparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Number Plate can not be blank")
    @NotEmpty(message = "Number Plate can not be blank")
    private String numberPlate;

    private String model;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ParkingSlot parkingSlot;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VehicleEntryExitStamp> entryExitStamps;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}