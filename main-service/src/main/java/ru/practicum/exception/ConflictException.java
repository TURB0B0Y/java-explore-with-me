package ru.practicum.exception;

public class ConflictException extends APIException {

    public ConflictException(String message, Object... args) {
        super(message, args);
    }

}
