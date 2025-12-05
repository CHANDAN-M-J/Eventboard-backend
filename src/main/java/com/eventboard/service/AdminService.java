package com.eventboard.service;

import com.eventboard.entity.Status;
import com.eventboard.entity.User;
import com.eventboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String updateUserStatus(Long userId, String status) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return "User not found!";
        }

        try {
            Status newStatus = Status.valueOf(status.toUpperCase());
            user.get().setStatus(newStatus);
            userRepository.save(user.get());
            return "User status updated to: " + newStatus;
        } catch (IllegalArgumentException e) {
            return "Invalid status! Use ACTIVE or INACTIVE.";
        }
    }
}
