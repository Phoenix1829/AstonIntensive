package ru.phoenix.notificationservice.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.phoenix.notificationservice.dto.EmailRequestDto;
import ru.phoenix.notificationservice.service.EmailService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    @PostMapping
    public void sendEmail(
            @Valid @RequestBody EmailRequestDto dto
    ) {

        emailService.send(
                dto.getEmail(),
                dto.getOperation()
        );

    }
}