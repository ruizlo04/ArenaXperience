package com.example.ApiArenaXperience.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de verificación");
        message.setText("Tu código de verificación es: "+verificationCode);

        emailSender.send(message);
    }
}
