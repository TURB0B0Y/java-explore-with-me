package ru.practicum.exception;

public class BadRequestException extends APIException {

    public BadRequestException(String message, Object... args) {
        super(message, args);
    }

}
