package ru.phoenix.userservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.phoenix.userservice.dto.EmailRequestDto;

@Component
public class NotificationClient {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationClient.class);

    private final RestClient restClient;

    public NotificationClient(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://NOTIFICATION-SERVICE")
                .build();
    }

    @CircuitBreaker(
            name = "notificationService",
            fallbackMethod = "fallbackSendEmail"
    )
    public void sendEmail(EmailRequestDto dto) {

        restClient.post()
                .uri("/api/notifications")
                .body(dto)
                .retrieve()
                .toBodilessEntity();

        log.info("REST notification sent.");

    }

    public void fallbackSendEmail(
            EmailRequestDto dto,
            Exception ex
    ) {

        log.warn("Notification-service unavailable: {}",
                ex.getMessage()
        );

    }

}