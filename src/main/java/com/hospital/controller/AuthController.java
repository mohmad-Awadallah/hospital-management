package com.hospital.controller;

import com.hospital.dto.LoginRequest;
import com.hospital.dto.SignupRequest;
import com.hospital.model.Role;
import com.hospital.response.JwtResponse;
import com.hospital.response.SuccessResponse;
import com.hospital.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login request for user: {}", loginRequest.getUsername());

        try {
            String jwt = authService.authenticateUser(loginRequest);
            Set<String> roles = authService.getUserRoles(loginRequest.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt, roles));
        } catch (Exception e) {
            logger.error("Login failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse("Invalid username or password", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        logger.info("Register request for user: {}", signupRequest.getUsername());

        try {
            authService.registerUser(signupRequest);

            Set<String> roles = authService.getUserRoles(signupRequest.getUsername());

            SuccessResponse response = new SuccessResponse(
                    LocalDateTime.now().format(dateTimeFormatter),
                    "User registered successfully!",
                    "OK",
                    roles
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", signupRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SuccessResponse(
                            LocalDateTime.now().format(dateTimeFormatter),
                            "Registration failed: " + e.getMessage(),
                            "ERROR",
                            null
                    ));
        }
    }
}