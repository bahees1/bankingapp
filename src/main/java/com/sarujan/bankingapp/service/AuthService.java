package com.sarujan.bankingapp.service;

import com.sarujan.bankingapp.model.User;
import com.sarujan.bankingapp.repository.UserRepository;
import com.sarujan.bankingapp.security.JwtUtility;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtility jwtUtility) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtility = jwtUtility;
    }

    // Register a new user
    public String register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(newUser);

        return jwtUtility.generateToken(username);
    }

    // Login existing user
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtUtility.generateToken(username);
    }
}