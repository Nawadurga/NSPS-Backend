package com.project.carparking.controller;

import com.project.carparking.config.AppConstants;
import com.project.carparking.dto.UserRequest;
import com.project.carparking.dto.UserResponse;
import com.project.carparking.dto.WithPaginationResponse;
import com.project.carparking.entity.User;
import com.project.carparking.entity.Vehicle;
import com.project.carparking.exception.ResourceNotFoundException;
import com.project.carparking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<WithPaginationResponse<UserResponse>> fetchUserList(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {

        return ResponseEntity.ok(userService.findAll(pageNo, pageSize));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String query, @RequestParam String searchBy) {

            List<UserResponse> userResponses = userService.searchUser(query, searchBy);
            return ResponseEntity.ok(userResponses);

    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserRequest> updateUserDetails(@PathVariable Long userId,@Valid @RequestBody UserRequest updatedUser) {
        UserRequest updatedUserData = userService.updateUserDetails(userId, updatedUser);

        return new ResponseEntity<>(updatedUserData, HttpStatus.OK);
    }

    @PostMapping("/{userId}/vehicles")
    public ResponseEntity<String> addVehicleToUser(@PathVariable Long userId, @RequestBody Vehicle vehicle) {

        userService.addVehicleToUser(userId, vehicle);
        return ResponseEntity.ok("Vehicle added to user successfully");

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}