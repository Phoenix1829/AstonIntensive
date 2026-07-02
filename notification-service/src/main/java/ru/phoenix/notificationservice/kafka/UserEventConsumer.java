package ru.phoenix.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.phoenix.notificationservice.event.UserEvent;
import ru.phoenix.notificationservice.service.EmailService;

@Component
public class UserEventConsumer {

    private final EmailService emailService;

    public UserEventConsumer(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "user-events",
            groupId = "notification-group"
    )
    public void consume(UserEvent event) {

        System.out.println("Получено сообщение:");
        System.out.println(event.getOperation());
        System.out.println(event.getEmail());

        emailService.send(event);
    }
}