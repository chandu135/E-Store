package com.company.e_store.service;

import com.company.e_store.dto.AuthResponse;
import com.company.e_store.dto.LoginRequest;
import com.company.e_store.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
