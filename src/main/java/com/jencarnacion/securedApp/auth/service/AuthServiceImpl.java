package com.jencarnacion.securedApp.auth.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jencarnacion.securedApp.auth.dto.AuthResponse;
import com.jencarnacion.securedApp.auth.dto.LoginRequest;
import com.jencarnacion.securedApp.auth.dto.RegisterRequest;
import com.jencarnacion.securedApp.role.Role;
import com.jencarnacion.securedApp.role.enums.RoleName;
import com.jencarnacion.securedApp.role.repository.RoleRepository;
import com.jencarnacion.securedApp.security.jwt.service.CustomUserDetailsService;
import com.jencarnacion.securedApp.security.jwt.service.JwtService;
import com.jencarnacion.securedApp.user.User;
import com.jencarnacion.securedApp.user.enums.AuthProvider;
import com.jencarnacion.securedApp.user.mapper.UserMapper;
import com.jencarnacion.securedApp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role USER doesn't exists"));

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setProvider(AuthProvider.LOCAL);
        user.setRoles(Set.of(role));

        userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                "Bearer",
                userMapper.toResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                "Bearer",
                userMapper.toResponse(user));
    }
}
