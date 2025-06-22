package com.simplewebapp.service;

import java.util.Optional;
import com.simplewebapp.model.User;

public interface UserService {
    User registerUser(String username, String password, String email);
    Optional<User> findByUsername(String username);
    boolean validateUser(String username, String password);
    boolean initiatePasswordReset(String email);
    boolean resetPassword(String token, String newPassword);
} 