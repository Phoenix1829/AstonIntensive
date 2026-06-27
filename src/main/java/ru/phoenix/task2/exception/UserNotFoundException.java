package ru.phoenix.task2.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found. Id = " + id);
    }
}