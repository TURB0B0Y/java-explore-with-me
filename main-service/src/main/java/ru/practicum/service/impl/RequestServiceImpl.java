package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.RequestDTO;
import ru.practicum.dto.request.UpdateRequestDTO;
import ru.practicum.dto.request.UpdateRequestResultDTO;
import ru.practicum.enums.EventState;
import ru.practicum.enums.StatusRequest;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.RequestService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDTO> getUserEventRequests(int userId, int eventId) {
        Event event = getEventById(eventId);
        return requestRepository.findAllByEvent(event).stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UpdateRequestResultDTO updateRequests(int userId, int eventId, UpdateRequestDTO dto) {
        Event event = getEventById(eventId);
        long eventConfirmedRequests = requestRepository.getEventRequestCountByStatus(eventId, StatusRequest.CONFIRMED);
        boolean isConfirmed = StatusRequest.CONFIRMED.equals(dto.getStatus());
        if (
                isConfirmed && event.getParticipantLimit() > 0
                        && event.getParticipantLimit() <= eventConfirmedRequests
        ) {
            throw new ConflictException("The participant limit has been reached");
        }

        List<Request> requests = requestRepository.findAllById(dto.getRequestIds());
        if (requests.stream().anyMatch(request -> !request.getStatus().equals(StatusRequest.PENDING)))
            throw new ConflictException("Request must have status PENDING");

        for (Request request : requests) {
            if (
                    isConfirmed
                            && (event.getParticipantLimit() < 1 || event.getParticipantLimit() > eventConfirmedRequests)
            ) {
                request.setStatus(StatusRequest.CONFIRMED);
                eventConfirmedRequests++;
            } else {
                request.setStatus(StatusRequest.REJECTED);
            }
        }
        UpdateRequestResultDTO.UpdateRequestResultDTOBuilder builder = UpdateRequestResultDTO.builder();
        for (Request request : requests) {
            if (StatusRequest.CONFIRMED.equals(request.getStatus()))
                builder.confirmedRequest(RequestMapper.toDto(request));
            else
                builder.rejectedRequest(RequestMapper.toDto(request));
        }
        return builder.build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDTO> getUserRequests(int userId) {
        User requester = getUserById(userId);
        return requestRepository.findAllByRequester(requester).stream()
                .map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDTO createRequest(int userId, int eventId) {
        Event event = getEventById(eventId);
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("Event not published");
        }
        if (event.getInitiator().getId().equals(userId))
            throw new ConflictException("initiator cant create request");
        if (event.getParticipantLimit() != 0) {
            long eventRequests = requestRepository.getEventRequestCountByStatus(eventId, StatusRequest.CONFIRMED);
            if (event.getParticipantLimit() <= eventRequests)
                throw new ConflictException("event participant limit");
        }
        User requester = getUserById(userId);
        if (requestRepository.countByEventAndRequesterAndStatusIn(event, requester, Arrays.asList(StatusRequest.CONFIRMED, StatusRequest.PENDING)) > 0)
            throw new ConflictException("User already has pending request");
        Request request = new Request();
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(StatusRequest.PENDING);
        } else {
            request.setStatus(StatusRequest.CONFIRMED);
        }
        request.setEvent(event);
        request.setRequester(requester);
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public RequestDTO cancelRequest(int userId, int requestId) {
        Request request = getRequestById(requestId);
        request.setStatus(StatusRequest.CANCELED);
        return RequestMapper.toDto(request);
    }

    private Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EventServiceImpl.EVENT_NOT_FOUND_TEMPLATE, eventId));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=%s was not found", userId));
    }

    private Request getRequestById(int requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=%s was not found", requestId));
    }

}
