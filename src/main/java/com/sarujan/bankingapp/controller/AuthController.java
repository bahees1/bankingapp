package com.sarujan.bankingapp.controller;

import com.sarujan.bankingapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register
     * Registers a new user and returns a JWT token if successful.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email
    ) {
        String token = authService.register(username, password, email);

        // Response body is a simple JSON map:
        // { "token": "...", "message": "..." }
        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "User registered successfully"
        ));
    }

    /**
     * POST /api/auth/login
     * Validates credentials and returns a JWT token if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        String token = authService.login(username, password);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Login successful"
        ));
    }
}
