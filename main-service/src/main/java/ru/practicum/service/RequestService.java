package ru.practicum.service;

import ru.practicum.dto.request.RequestDTO;
import ru.practicum.dto.request.UpdateRequestDTO;
import ru.practicum.dto.request.UpdateRequestResultDTO;

import java.util.List;

public interface RequestService {

    List<RequestDTO> getUserEventRequests(int userId, int eventId);

    UpdateRequestResultDTO updateRequests(int userId, int eventId, UpdateRequestDTO dto);

    List<RequestDTO> getUserRequests(int userId);

    RequestDTO createRequest(int userId, int eventId);

    RequestDTO cancelRequest(int userId, int requestId);

}
