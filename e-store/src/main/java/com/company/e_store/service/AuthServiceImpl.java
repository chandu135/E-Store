package com.company.e_store.service;

import com.company.e_store.client.EmailClientService;
import com.company.e_store.dto.AuthResponse;
import com.company.e_store.dto.LoginRequest;
import com.company.e_store.dto.RegisterRequest;
import com.company.e_store.entity.User;
import com.company.e_store.repository.UserRepository;
import com.company.e_store.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

        private final EmailClientService emailClientService;

        public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailClientService emailClientService) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.jwtUtil = jwtUtil;
            this.emailClientService = emailClientService;
        }


        @Override
        public AuthResponse register(RegisterRequest request) {
            boolean exists = userRepository.findByEmail(request.getEmail()).isPresent();
            if (exists) {
                return new AuthResponse("Email already registered.", null, null);
            }

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);

            //String token = jwtUtil.generateToken(user.getEmail());

        // Send welcome email
        String emailStatus;
        try {
            emailStatus = emailClientService.sendWelcomeEmail(user.getEmail(), user.getName());
        } catch (Exception e) {
            emailStatus = "Email sending failed: " + e.getMessage();
            return new AuthResponse(emailStatus, "failure", null); // Return failure if email sending fails
        }

        return new AuthResponse("User registered successfully. " + emailStatus, "success", null);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword())) // TODO: match encoded password
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getEmail());
                    return new AuthResponse("Login successful", null, token);
                })
                .orElseGet(() -> new AuthResponse("Invalid email or password.", null, null));
    }
}
