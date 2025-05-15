package com.company.emailservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment("attachment.pdf", new ClassPathResource(pathToAttachment));
        mailSender.send(message);
    }

    public void sendTemplateEmail(String to, String subject, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        String html = templateEngine.process("welcome", context);
        sendHtmlEmail(to, subject, html);
    }

    // Example scheduled email (every day at 9 AM)
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendScheduledEmail() {
        sendSimpleEmail("test@example.com", "Daily Reminder", "This is your daily email reminder.");
    }

}
