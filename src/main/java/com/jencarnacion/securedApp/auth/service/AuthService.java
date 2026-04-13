package com.jencarnacion.securedApp.auth.service;

import com.jencarnacion.securedApp.auth.dto.AuthResponse;
import com.jencarnacion.securedApp.auth.dto.LoginRequest;
import com.jencarnacion.securedApp.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
