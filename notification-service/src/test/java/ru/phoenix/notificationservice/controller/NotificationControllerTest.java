package ru.phoenix.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.phoenix.notificationservice.dto.EmailRequestDto;
import ru.phoenix.notificationservice.event.OperationType;
import ru.phoenix.notificationservice.service.EmailService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    EmailService emailService;

    @Test
    void sendEmail_ShouldCallService() throws Exception {

        EmailRequestDto dto = new EmailRequestDto();

        dto.setEmail("user@test.com");
        dto.setOperation(OperationType.CREATE);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(emailService)
                .send("user@test.com", OperationType.CREATE);
    }

    @Test
    void sendEmail_InvalidEmail_ShouldReturnBadRequest() throws Exception {

        EmailRequestDto dto = new EmailRequestDto();

        dto.setEmail("invalid-email");
        dto.setOperation(OperationType.CREATE);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(emailService);
    }

    @Test
    void sendEmail_WithoutOperation_ShouldReturnBadRequest() throws Exception {

        String json = """
        {
          "email":"user@test.com"
        }
        """;

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(emailService);
    }
}
