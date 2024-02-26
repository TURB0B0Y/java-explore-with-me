package ru.practicum.mapper;

import ru.practicum.dto.request.RequestDTO;
import ru.practicum.model.Request;

public class RequestMapper {

    public static RequestDTO toDto(Request request) {
        RequestDTO dto = new RequestDTO();
        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setCreated(request.getCreated());
        dto.setStatus(request.getStatus());
        dto.setEvent(request.getEvent().getId());
        return dto;
    }

}
