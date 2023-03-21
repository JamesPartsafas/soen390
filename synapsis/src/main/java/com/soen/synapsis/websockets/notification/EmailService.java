package com.soen.synapsis.websockets.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * A Spring component class that provides email sending functionality using JavaMailSender.
 */
@Component
public class EmailService {
    private JavaMailSender emailSender;

    /**
     * Creates a new EmailService instance with the specified JavaMailSender instance.
     * This is annotated by autowired for automatic dependency injection
     * @param emailSender the JavaMailSender instance to use for sending email messages
     */
    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends a simple email message with the specified recipient email address, subject, and text.
     * @param to the email address of the recipient
     * @param subject the subject of the email message
     * @param text the text content of the email message
     * @throws MailException if an error occurs while sending the email message
     */
    public void sendSimpleMessage(String to, String subject, String text) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("synapsis390@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
