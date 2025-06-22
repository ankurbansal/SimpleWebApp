package com.simplewebapp.service.impl;

import com.simplewebapp.model.User;
import com.simplewebapp.repository.UserRepository;
import com.simplewebapp.service.EMailSenderService;
import com.simplewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EMailSenderService emailSenderService;

    private ConcurrentHashMap<String, String> resetTokens = new ConcurrentHashMap<>(); // token -> email

    @Override
    public User registerUser(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User(username, passwordEncoder.encode(password), email);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean validateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    @Override
    public boolean initiatePasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            String token = UUID.randomUUID().toString();
            resetTokens.put(token, email);
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            emailSenderService.sendResetEmail(email, resetLink);
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        String email = resetTokens.get(token);
        if (email != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                resetTokens.remove(token);
                return true;
            }
        }
        return false;
    }
} 