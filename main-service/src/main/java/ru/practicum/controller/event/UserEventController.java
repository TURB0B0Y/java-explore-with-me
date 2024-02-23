package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.CreateEventDTO;
import ru.practicum.dto.event.EventDTO;
import ru.practicum.dto.event.ShortEventDTO;
import ru.practicum.dto.event.UpdateEventDTO;
import ru.practicum.dto.request.RequestDTO;
import ru.practicum.dto.request.UpdateRequestDTO;
import ru.practicum.dto.request.UpdateRequestResultDTO;
import ru.practicum.filter.PageFilter;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public List<ShortEventDTO> getEvents(@PathVariable int userId, @Valid PageFilter pageFilter) {
        log.info("getEvents {} {}", userId, pageFilter);
        return eventService.getAll(userId, pageFilter);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO createEvent(@PathVariable int userId, @RequestBody @Valid CreateEventDTO dto) {
        log.info("createEvent {} {}", userId, dto);
        return eventService.createEvent(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventDTO getUserEventById(@PathVariable int userId, @PathVariable int eventId) {
        log.info("getUserEventById {} {}", eventId, userId);
        return eventService.getByInitiatorAndId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDTO updateUserEvent(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody @Valid UpdateEventDTO dto
    ) {
        log.info("updateUserEvent {} {} {}", eventId, userId, dto);
        return eventService.updateEvent(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDTO> getEventRequests(
            @PathVariable int userId,
            @PathVariable int eventId
    ) {
        log.info("getEventRequests {} {}", userId, eventId);
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public UpdateRequestResultDTO updateEventRequests(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody @Valid UpdateRequestDTO dto
    ) {
        log.info("updateEventRequests {} {} {}", userId, eventId, dto);
        return requestService.updateRequests(userId, eventId, dto);
    }

}
