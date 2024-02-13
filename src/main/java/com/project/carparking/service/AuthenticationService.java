package com.project.carparking.service;


import com.project.carparking.config.JwtService;
import com.project.carparking.dto.AuthenticationRequest;
import com.project.carparking.dto.AuthenticationResponse;
import com.project.carparking.dto.RegisterRequest;
import com.project.carparking.entity.EnumRole;
import com.project.carparking.entity.User;
import com.project.carparking.exception.ResourceAlreadyExistException;
import com.project.carparking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;




    public static String generateOtp() {
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10);
            otpBuilder.append(digit);
        }

        return otpBuilder.toString();
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        List<User> adminUsers = repository.findByRole(EnumRole.ADMIN);

        if (!adminUsers.isEmpty()) {
            throw new ResourceAlreadyExistException("Admin user already exists");
        }

        var user = User.builder()
                .name(request.getName())
                .phoneNo(request.getPhoneNo())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(EnumRole.ADMIN)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateAdmin(AuthenticationRequest request) {
        var user = repository.findByPhoneNo(request.getPhoneNo())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() != EnumRole.ADMIN) {
            throw new AccessDeniedException("Wrong Credential!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNo(),
                        request.getPassword()
                )
        );


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .phoneNo(user.getPhoneNo())
                .name(user.getName())
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> userByPhoneNo = repository.findByPhoneNo(request.getPhoneNo());
        if (userByPhoneNo.isPresent()) {
            throw new ResourceAlreadyExistException("Phone number already exist!");
        }


        var user = User.builder()
                .name(request.getName())
                .phoneNo(request.getPhoneNo())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(EnumRole.USER)
                .build();

        User dbUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(dbUser.getId())
                .phoneNo(dbUser.getPhoneNo())
                .name(dbUser.getName())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNo(),
                        request.getPassword()
                )
        );
        var user = repository.findByPhoneNo(request.getPhoneNo())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .phoneNo(user.getPhoneNo())
                .name(user.getName())
                .isAdmin(user.getRole() == EnumRole.ADMIN)
                .build();
    }


}