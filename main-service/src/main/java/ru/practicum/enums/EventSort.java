package ru.practicum.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventSort {

    EVENT_DATE("eventDate"),
    VIEWS("views");

    private final String column;

}
