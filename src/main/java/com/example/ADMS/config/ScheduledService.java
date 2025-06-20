package com.example.ADMS.config;

import com.example.ADMS.repository.TokenRepository;
import com.example.ADMS.utils.DateTimeHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduledService {
    TokenRepository tokenRepository;

    @Scheduled(fixedDelayString = "${application.security.jwt.schedule.expiration}")
    public void scheduleFixedDelayTask() {
        var validTokens = tokenRepository.findAllValidToken();
        if (validTokens.isEmpty()) return;
        validTokens.forEach(t -> {
            if (t.getExpiredAt().isBefore(DateTimeHelper.getTimeNow())) {
                t.setExpired(true);
                t.setRevoked(true);
            }
        });
        tokenRepository.saveAll(validTokens);
        log.info("Expired tokens were deleted");
    }
}
