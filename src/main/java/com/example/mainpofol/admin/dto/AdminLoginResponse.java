package com.example.mainpofol.admin.dto;

public record AdminLoginResponse(
        String tokenType,
        String accessToken,
        long expiresIn
) {
}
