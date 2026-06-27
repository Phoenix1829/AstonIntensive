package ru.phoenix.task2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.phoenix.task2.dto.UserRequestDto;
import ru.phoenix.task2.dto.UserResponseDto;
import ru.phoenix.task2.entity.User;
import ru.phoenix.task2.exception.UserNotFoundException;
import ru.phoenix.task2.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDto create(
            UserRequestDto dto
    ) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());

        User saved =
                repository.save(user);

        return mapToDto(saved);
    }

    public UserResponseDto getById(Long id) {

        User user = repository.findById(id)
                        .orElseThrow(
                                () -> new UserNotFoundException(id)
                        );

        return mapToDto(user);
    }

    public List<UserResponseDto> getAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private UserResponseDto mapToDto(
            User user
    ) {

        UserResponseDto dto =
                new UserResponseDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public UserResponseDto update(
            Long id,
            UserRequestDto dto
    ) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());

        User updated = repository.save(user);

        return mapToDto(updated);
    }
}