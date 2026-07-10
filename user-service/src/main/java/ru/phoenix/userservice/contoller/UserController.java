package ru.phoenix.userservice.contoller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.phoenix.userservice.dto.UserRequestDto;
import ru.phoenix.userservice.dto.UserResponseDto;
import ru.phoenix.userservice.hateoas.UserModelAssembler;
import ru.phoenix.userservice.service.UserService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "Users",
        description = "API для управления пользователями"
)
public class UserController {

    private final UserService service;
    private final UserModelAssembler assembler;

    public UserController(
            UserService service, UserModelAssembler assembler
    ) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public EntityModel<UserResponseDto> create(
            @Valid @RequestBody UserRequestDto dto
    ) {
        return assembler.toModel(service.create(dto));
    }

    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public EntityModel<UserResponseDto> getById(
            @PathVariable Long id
    ) {
        return assembler.toModel(service.getById(id));
    }

    @Operation(summary = "Получить список пользователей")
    @GetMapping
    public CollectionModel<EntityModel<UserResponseDto>> getAll() {

        List<EntityModel<UserResponseDto>> users =
                service.getAll()
                        .stream()
                        .map(assembler::toModel)
                        .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class)
                        .getAll())
                        .withSelfRel());
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить пользователя")
    @PutMapping("/{id}")
    public EntityModel<UserResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto
    ) {
        return assembler.toModel(service.update(id, dto));
    }
}