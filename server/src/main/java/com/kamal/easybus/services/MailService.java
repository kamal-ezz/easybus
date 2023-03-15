package com.kamal.easybus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String from;

    public void sendWelcomeEmail(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("Welcome to Easybus!");
        helper.setText("Dear user,\n\nThank you for signing up for My App. We're excited to have you onboard!\n\nBest regards,\nMy App Team");
        mailSender.send(message);
    }

}
