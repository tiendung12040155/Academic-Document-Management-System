package com.example.ADMS.utils;


import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.EmailInfo;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHandler {
    private final JavaMailSender javaMailSender;
    private final MessageException messageException;
    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void send(EmailInfo emailInfo) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(emailInfo.getTo());
            helper.setSubject(emailInfo.getSubject());
            helper.setText(emailInfo.getContent(), true);
            javaMailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            throw ApiException.internalServerException(messageException.MSG_ERROR_SENDING_MAIL);
        }
    }
}
