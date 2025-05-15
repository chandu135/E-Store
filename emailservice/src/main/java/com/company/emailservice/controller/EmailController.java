package com.company.emailservice.controller;

import com.company.emailservice.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-simple")
    public String sendSimpleEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        emailService.sendSimpleEmail(to, subject, text);
        return "Simple Email Sent!";
    }

    @PostMapping("/send-html")
    public String sendHtmlEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String html) throws MessagingException {
        emailService.sendHtmlEmail(to, subject, html);
        return "HTML Email Sent!";
    }

    @PostMapping("/send-template")
    public String sendTemplateEmail(@RequestParam String to, @RequestParam String name) throws MessagingException {
        emailService.sendTemplateEmail(to, "Welcome Email", name);
        return "Template Email Sent!";
    }

    @PostMapping("/send-text")
    public String sendTextEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        try {
            emailService.sendSimpleEmail(to, subject, text);
            return "Text Email Sent Successfully!";
        } catch (Exception e) {
            return "Failed to send text email: " + e.getMessage();
        }
    }
}
