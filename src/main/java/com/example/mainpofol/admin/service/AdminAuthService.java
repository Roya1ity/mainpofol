package com.example.mainpofol.admin.service;

import com.example.mainpofol.admin.dto.AdminLoginRequest;
import com.example.mainpofol.admin.dto.AdminLoginResponse;
import com.example.mainpofol.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${admin.password:}")
    private String adminPassword;

    @Value("${jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    public AdminLoginResponse login(AdminLoginRequest request) {
        if (!StringUtils.hasText(adminPassword)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin password is not configured.");
        }

        if (!adminPassword.equals(request.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin password.");
        }

        String accessToken = jwtTokenProvider.createAdminToken();
        return new AdminLoginResponse("Bearer", accessToken, expirationSeconds);
    }
}
