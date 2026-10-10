package com.example.vehicleservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.example.vehicleservice.dto.LoginRequestDto;
import com.example.vehicleservice.dto.LoginResponseDto;
import com.example.vehicleservice.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.authenticationManager =
                authenticationManager;

        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {

        Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                    )
                );

        String username =
                authentication.getName();

        String token =
                jwtService.generateToken(username);

        LoginResponseDto response =
                new LoginResponseDto(
                    token,
                    username,
                    "Login successful"
                );

        return ResponseEntity.ok(response);
    }
}