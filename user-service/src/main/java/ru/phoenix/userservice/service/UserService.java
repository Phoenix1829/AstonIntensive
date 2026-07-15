package ru.phoenix.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.phoenix.userservice.client.NotificationClient;
import ru.phoenix.userservice.dto.EmailRequestDto;
import ru.phoenix.userservice.dto.UserRequestDto;
import ru.phoenix.userservice.dto.UserResponseDto;
import ru.phoenix.userservice.entity.User;
import ru.phoenix.userservice.event.OperationType;
import ru.phoenix.userservice.event.UserEvent;
import ru.phoenix.userservice.exception.UserNotFoundException;
import ru.phoenix.userservice.kafka.UserEventProducer;
import ru.phoenix.userservice.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final UserEventProducer producer;
    private final NotificationClient notificationClient;

    public UserService(UserRepository repository, UserEventProducer producer, NotificationClient notificationClient) {
        this.repository = repository;
        this.producer = producer;
        this.notificationClient = notificationClient;
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

        producer.send(
                new UserEvent(
                        OperationType.CREATE,
                        saved.getEmail()
                )
        );

        notificationClient.sendEmail(
                new EmailRequestDto(
                        saved.getEmail(),
                        OperationType.CREATE
                )
        );

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

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id));

        repository.delete(user);

        producer.send(
                new UserEvent(
                        OperationType.DELETE,
                        user.getEmail()
                )
        );

        notificationClient.sendEmail(
                new EmailRequestDto(
                        user.getEmail(),
                        OperationType.DELETE
                )
        );
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