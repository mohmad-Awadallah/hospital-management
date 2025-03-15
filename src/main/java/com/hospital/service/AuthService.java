package com.hospital.service;

import com.hospital.dto.LoginRequest;
import com.hospital.dto.SignupRequest;

import java.util.Set;

public interface AuthService {

    public String authenticateUser(LoginRequest loginRequest);

    public void registerUser(SignupRequest signupRequest);

    public Set<String> getUserRoles(String username);
}
