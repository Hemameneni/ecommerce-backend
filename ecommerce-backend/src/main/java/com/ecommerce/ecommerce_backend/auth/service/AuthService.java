package com.ecommerce.ecommerce_backend.auth.service;




import com.ecommerce.ecommerce_backend.auth.dto.AuthResponse;
import com.ecommerce.ecommerce_backend.auth.dto.LoginRequest;
import com.ecommerce.ecommerce_backend.auth.dto.RegisterRequest;
import com.ecommerce.ecommerce_backend.user.Role;
import com.ecommerce.ecommerce_backend.user.entity.User;
import com.ecommerce.ecommerce_backend.user.repository.UserRepository;
import com.ecommerce.ecommerce_backend.security.JwtUtil;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    // REGISTER already exists

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token);
    }


    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

Role role=Role.valueOf(request.getRole().toUpperCase());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
    }
}

