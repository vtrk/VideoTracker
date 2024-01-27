package com.vtrk.videotracker.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
    Email service
 */
@Service
public class EmailService {

    /**
     * Mail sender
     * Autowired by Spring
     * Uses application.properties to get mail settings
     */
    @Autowired
    private JavaMailSender mailSender;


    /**
     * Send email
     * @param to email address
     * @param subject subject
     * @param text text
     */
    public void sendEmail(String to, String subject, String text) {
        org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
        message.setFrom("noreply@videotracker.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setSentDate(new java.util.Date());
        mailSender.send(message);
    }



}
