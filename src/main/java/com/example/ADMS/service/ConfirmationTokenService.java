package com.example.ADMS.service;

import com.example.ADMS.entity.User;

public interface ConfirmationTokenService {
    String generateTokenEmail(User user);

    String goToForgotPassword(String token);
}
