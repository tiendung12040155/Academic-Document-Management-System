package com.example.ADMS.service.impl;

import com.example.ADMS.entity.ConfirmationToken;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.repository.ConfirmTokenRepository;
import com.example.ADMS.service.ConfirmationTokenService;
import com.example.ADMS.utils.Constants;
import com.example.ADMS.utils.DateTimeHelper;
import com.example.ADMS.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    ConfirmTokenRepository confirmTokenRepository;
    MessageException messageException;

    @Override
    public String generateTokenEmail(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken ct = ConfirmationToken.builder()
                .token(token)
                .createdAt(DateTimeHelper.getTimeNow())
                .expiresAt(DateTimeHelper.getTimeNow().plusMinutes(Constants.EMAIL_WAITING_EXPIRATION))
                .user(user)
                .build();
        confirmTokenRepository.save(ct);
        return token;
    }

    @Override
    public String goToForgotPassword(String token) {
        ConfirmationToken confirmationToken = confirmTokenRepository.findByToken(token)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TOKEN_NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            throw ApiException.badRequestException(messageException.MSG_USER_EMAIL_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(DateTimeHelper.getTimeNow())) {
            throw ApiException.badRequestException(messageException.MSG_TOKEN_EXPIRED);
        }
        //link front-end
        return "confirm-password";
    }
}

