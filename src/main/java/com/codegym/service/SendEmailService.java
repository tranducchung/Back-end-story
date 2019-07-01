package com.codegym.service;

import org.springframework.mail.SimpleMailMessage;

public interface SendEmailService {
    void sendEmail(SimpleMailMessage simpleMailMessage);
}
