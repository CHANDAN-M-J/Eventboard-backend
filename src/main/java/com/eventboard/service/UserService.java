package com.eventboard.service;

import com.eventboard.dto.LoginRequest;
import com.eventboard.dto.RegisterRequest;
import com.eventboard.entity.Role;
import com.eventboard.entity.Status;
import com.eventboard.entity.User;
import com.eventboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);

        userRepository.save(user);
        return "User registered successfully!";
    }

    public String login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            return "Invalid email!";
        }

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return "Invalid password!";
        }

        return "Login successful!";
    }
}
