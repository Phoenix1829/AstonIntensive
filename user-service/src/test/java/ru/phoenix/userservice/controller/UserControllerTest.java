package ru.phoenix.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.phoenix.userservice.UserServiceApplication;
import ru.phoenix.userservice.contoller.UserController;
import ru.phoenix.userservice.dto.UserRequestDto;
import ru.phoenix.userservice.dto.UserResponseDto;
import ru.phoenix.userservice.hateoas.UserModelAssembler;
import ru.phoenix.userservice.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import(UserModelAssembler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserById_ShouldReturnUser()
            throws Exception {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(1L);
        dto.setName("Mikhail");
        dto.setEmail("mikhail@test.com");
        dto.setAge(23);
        dto.setCreatedAt(LocalDateTime.now());

        when(service.getById(1L))
                .thenReturn(dto);

        mockMvc.perform(
                        get("/api/users/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(1)
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Mikhail")
                )
                .andExpect(
                        jsonPath("$._links.self.href").exists())
                .andExpect(
                        jsonPath("$._links.allUsers.href").exists())
                .andExpect(
                        jsonPath("$._links.update.href").exists())
                .andExpect(
                        jsonPath("$._links.delete.href").exists());
    }

    @Test
    void getAll_ShouldReturnUsers()
            throws Exception {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(1L);
        dto.setName("Mikhail");

        when(service.getAll())
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get("/api/users")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$._embedded.userResponseDtoList[0].id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$._links.self.href").exists()
                );
    }

    @Test
    void create_ShouldCreateUser()
            throws Exception {

        UserRequestDto request = new UserRequestDto();

        request.setName("Mikhail");
        request.setEmail("mikhail@test.com");
        request.setAge(25);

        UserResponseDto response = new UserResponseDto();

        response.setId(1L);
        response.setName("Mikhail");

        when(service.create(any(UserRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/users")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$._links.self.href").exists())
                .andExpect(
                        jsonPath("$._links.update.href").exists())
                .andExpect(
                        jsonPath("$._links.delete.href").exists())
                .andExpect(
                        jsonPath("$._links.allUsers.href").exists());
    }

    @Test
    void update_ShouldUpdateUser()
            throws Exception {

        UserRequestDto request = new UserRequestDto();

        request.setName("Updated");
        request.setEmail("updated@test.com");
        request.setAge(30);

        UserResponseDto response = new UserResponseDto();

        response.setId(1L);
        response.setName("Updated");

        when(service.update(
                eq(1L),
                any(UserRequestDto.class)
        ))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/users/1")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("Updated")
                )
                .andExpect(
                        jsonPath("$._links.self.href").exists())
                .andExpect(
                        jsonPath("$._links.update.href").exists())
                .andExpect(
                        jsonPath("$._links.delete.href").exists());
    }

    @Test
    void delete_ShouldDeleteUser()
            throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/api/users/1")
                )
                .andExpect(status().isNoContent());
    }
}