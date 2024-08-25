package com.allclear.socialhub.user.service;

import com.allclear.socialhub.user.type.EmailType;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendEmail(String email, EmailType emailType) throws MessagingException;

    String getVerificationToken(String email);
    
}
