package com.ecommerce.ecommerce_backend.auth.controller;




import com.ecommerce.ecommerce_backend.auth.dto.AuthResponse;
import com.ecommerce.ecommerce_backend.auth.dto.LoginRequest;
import com.ecommerce.ecommerce_backend.auth.dto.RegisterRequest;
import com.ecommerce.ecommerce_backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest request) {

        authService.register(request);
    }



    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }


}

