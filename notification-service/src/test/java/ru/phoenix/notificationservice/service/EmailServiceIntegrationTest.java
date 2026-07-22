package ru.phoenix.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import ru.phoenix.notificationservice.NotificationServiceApplication;
import ru.phoenix.notificationservice.event.OperationType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = NotificationServiceApplication.class,
        properties = {
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.mail.username=test@test.com"})
@TestPropertySource(properties = {
        "spring.mail.username=test@test.com"
})
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @MockitoBean
    private JavaMailSender mailSender;

    @Test
    void sendCreateEmail_ShouldSendCorrectMessage() {

        emailService.send(
                "user@test.com",
                OperationType.CREATE
        );

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(
                "user@test.com",
                message.getTo()[0]
        );

        assertEquals(
                "Уведомление",
                message.getSubject()
        );

        assertEquals(
                "Здравствуйте! Ваш аккаунт на сайте был успешно создан.",
                message.getText()
        );

        assertEquals(
                "test@test.com",
                message.getFrom()
        );
    }

    @Test
    void sendDeleteEmail_ShouldSendCorrectMessage() {

        emailService.send(
                "user@test.com",
                OperationType.DELETE
        );

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(
                "Здравствуйте! Ваш аккаунт был удалён.",
                message.getText()
        );
    }
}
