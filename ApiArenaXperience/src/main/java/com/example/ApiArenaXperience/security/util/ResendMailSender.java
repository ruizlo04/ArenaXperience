package com.example.ApiArenaXperience.security.util;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResendMailSender {

    @Value("${resend.api.key}")
    private String resendApiKey;

    private Resend resend;

    @PostConstruct
    public void init() {
        if (resendApiKey == null || resendApiKey.isEmpty()) {
            throw new IllegalStateException("API Key de Resend no está configurada.");
        }
        this.resend = new Resend(resendApiKey);
    }

    @Async
    public void sendMail(String to, String subject, String message) throws IOException, ResendException {
        if (resend == null) {
            throw new IllegalStateException("Resend no ha sido inicializado correctamente.");
        }

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(to)
                .subject(subject)
                .html(message)
                .build();

        CreateEmailResponse data = resend.emails().send(params);
    }
}
