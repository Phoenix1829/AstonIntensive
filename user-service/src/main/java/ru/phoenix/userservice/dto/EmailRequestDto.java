package ru.phoenix.userservice.dto;

import ru.phoenix.userservice.event.OperationType;

public class EmailRequestDto {

    private String email;
    private OperationType operation;

    public EmailRequestDto() {
    }

    public EmailRequestDto(String email, OperationType operation) {
        this.email = email;
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }
}