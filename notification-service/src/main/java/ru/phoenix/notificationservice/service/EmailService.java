package ru.phoenix.notificationservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.phoenix.notificationservice.event.OperationType;
import ru.phoenix.notificationservice.event.UserEvent;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(UserEvent event) {
        send(event.getEmail(), event.getOperation());
    }

    public void send(String email, OperationType operation) {

        String text;

        if (operation == OperationType.CREATE) {
            text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else {
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Уведомление");
        message.setText(text);

        mailSender.send(message);
    }
}