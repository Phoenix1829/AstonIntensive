package ru.phoenix.task2.contoller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.phoenix.task2.dto.UserRequestDto;
import ru.phoenix.task2.dto.UserResponseDto;
import ru.phoenix.task2.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(
            UserService service
    ) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDto create(
            @Valid @RequestBody UserRequestDto dto
    ) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(
            @PathVariable Long id
    ) {
        return service.getById(id);
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto
    ) {
        return service.update(id, dto);
    }
}