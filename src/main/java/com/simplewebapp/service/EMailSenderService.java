package com.simplewebapp.service;

public interface EMailSenderService {
    void sendResetEmail(String to, String resetLink);
} 