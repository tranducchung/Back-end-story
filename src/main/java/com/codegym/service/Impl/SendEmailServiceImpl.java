package com.codegym.service.Impl;

import com.codegym.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        mailSender.send(simpleMailMessage);
    }
}
