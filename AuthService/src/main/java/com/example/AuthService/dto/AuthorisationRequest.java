package com.example.AuthService.dto;

public record AuthorisationRequest(
        String username,
        String password
) {
}