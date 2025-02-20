package com.example.ApiArenaXperience.security.util;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

public class ResendMailSender {

    private String resendApiKey;

    private Resend resend;

    public void init() {
        resend = new Resend(resendApiKey);
    }

    @Async
    public void sendMail(String to, String subject, String message) throws IOException, ResendException {

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("2dam <2dam@dam.salesianostriana.dev>")
                .to(to)
                .subject(subject)
                .html(message)
                .build();


        CreateEmailResponse data = resend.emails().send(params);

    }
}
