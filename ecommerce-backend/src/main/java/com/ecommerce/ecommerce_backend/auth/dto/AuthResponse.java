package com.ecommerce.ecommerce_backend.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public AuthResponse() {
    }
}

