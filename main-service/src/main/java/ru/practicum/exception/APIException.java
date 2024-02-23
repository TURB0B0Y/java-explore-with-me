package ru.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIException extends RuntimeException {

    public APIException(String message, Object... args) {
        super(String.format(message, args));
    }

}
