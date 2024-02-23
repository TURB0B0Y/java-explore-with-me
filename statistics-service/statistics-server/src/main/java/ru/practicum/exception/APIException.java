package ru.practicum.exception;

public class APIException extends RuntimeException {

    public APIException(String message, Object... args) {
        super(String.format(message, args));
    }

}
