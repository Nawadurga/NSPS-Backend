package com.project.carparking.service;

import com.project.carparking.dto.UserRequest;
import com.project.carparking.dto.UserResponse;
import com.project.carparking.dto.WithPaginationResponse;
import com.project.carparking.dto.converter.Converter;
import com.project.carparking.entity.EnumRole;
import com.project.carparking.entity.ParkingSlot;
import com.project.carparking.entity.User;
import com.project.carparking.entity.Vehicle;
import com.project.carparking.exception.ResourceNotFoundException;
import com.project.carparking.repository.ParkingSlotRepository;
import com.project.carparking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private NotificationTokenServices notificationTokenServices;

    @Autowired
    private NotificationService notificationService;



    public WithPaginationResponse<UserResponse> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);

        WithPaginationResponse<UserResponse> userAndPagination = new WithPaginationResponse<>();
        userAndPagination.getContent().addAll(userPage.stream()
                .map(Converter::convertToUserResponse)
                .toList());
        userAndPagination.setPaginationResponse(Converter.convertPageToPageResponse(userPage));

        return userAndPagination;
    }


    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new ResourceNotFoundException("User " + userId + " not found");
        });

        return Converter.convertToUserResponse(user);
    }

    public List<UserResponse> searchUser(String query, String searchBy) {
        List<UserResponse> userResponseList = new ArrayList<>();

        // Convert searchBy to lowercase
        searchBy = searchBy.toLowerCase();

        switch (searchBy) {
            case "name" -> {
                List<User> byName = userRepository.findByNameContaining(query);
                for (User user : byName) {
                    UserResponse userResponse = Converter.convertToUserResponse(user);
                    userResponseList.add(userResponse);
                }
            }
            case "phone_no" -> {
                Optional<User> byPhoneNo = userRepository.findByPhoneNoContaining(query);
                if (byPhoneNo.isPresent()) {
                    UserResponse userResponse = Converter.convertToUserResponse(byPhoneNo.get());
                    userResponseList.add(userResponse);
                } else {
                    throw new ResourceNotFoundException("User with phone number " + query + " not found");
                }
            }
            case "number_plate" -> {
                List<User> byVehicleNumber = userRepository.findByVehicleNumberPlateContaining(query);
                for (User user : byVehicleNumber) {
                    UserResponse userResponse = Converter.convertToUserResponse(user);
                    userResponseList.add(userResponse);
                }
            }
            case "parking_slot" -> {
                ParkingSlot parkingSlot = parkingSlotRepository.findBySlotNumber(query).orElseThrow(() -> {
                    return new ResourceNotFoundException("Parking Slot " + query + " not found");
                });


                    User user = parkingSlot.getVehicle().getUser();
                    UserResponse userResponse = Converter.convertToUserResponse(user);
                    userResponseList.add(userResponse);

            }
            default ->
                // Handle invalid searchBy parameter
                    throw new IllegalArgumentException("Invalid searchBy parameter: " + searchBy);
        }

        if (userResponseList.isEmpty()) {
            throw new ResourceNotFoundException("No users found with the provided " + searchBy);
        }

        return userResponseList;
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public User saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setRole(EnumRole.USER);
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);

        pushNotificationService.sendOnboardingNotification(savedUser.getId());
        return savedUser;
    }

    public UserRequest updateUserDetails(Long userId, UserRequest userRequest) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> {
            return new ResourceNotFoundException("User " + userId + " not found");
        });


        // Update user details from UserRequest
        if (userRequest.getName() != null) {
            existingUser.setName(userRequest.getName());
        }
        if (userRequest.getAddress() != null) {
            existingUser.setAddress(userRequest.getAddress());
        }
        if (userRequest.getPhoneNo() != null) {
            existingUser.setPhoneNo(userRequest.getPhoneNo());
        }


        userRepository.save(existingUser);
        return userRequest;

    }

    public void addVehicleToUser(Long userId, Vehicle vehicle) {
        // Retrieve the User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Set the User for the Vehicle
        vehicle.setUser(user);

        // Add the Vehicle to the User's list of vehicles
        user.getVehicles().add(vehicle);

        // Save the User to update the association
        userRepository.save(user);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new ResourceNotFoundException("User " + userId + " not found");
        });

        if (user.getRole() != EnumRole.ADMIN) {
            notificationTokenServices.deleteByUserId(userId);
            notificationService.deleteByUserId(userId);
            userRepository.deleteById(userId);
        }else {
            throw  new IllegalArgumentException("Admin cannot be deleted");

        }
    }


}
