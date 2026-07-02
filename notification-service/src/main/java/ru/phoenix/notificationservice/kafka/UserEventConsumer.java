package ru.phoenix.notificationservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.phoenix.notificationservice.event.UserEvent;
import ru.phoenix.notificationservice.service.EmailService;

@Component
public class UserEventConsumer {

    private final EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(UserEventConsumer.class);

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

        log.info("Получено сообщение {}", event);
        log.info("Operation: {}", event.getOperation());
        log.info("Email: {}", event.getEmail());

        emailService.send(event);
    }
}