package com.allclear.socialhub.user.service;

import com.allclear.socialhub.user.type.EmailType;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendEmail(String token, EmailType emailType);

}
