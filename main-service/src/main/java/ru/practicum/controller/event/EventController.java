package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventDTO;
import ru.practicum.dto.event.ShortEventDTO;
import ru.practicum.filter.EventFilter;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<ShortEventDTO> getEvents(@Valid EventFilter filter, HttpServletRequest request) {
        log.info("getEvents {}", filter);
        eventService.addStatistic(request);
        return eventService.findEvents(filter);
    }

    @GetMapping("/{eventId}")
    public EventDTO getEventById(@PathVariable int eventId, HttpServletRequest request) {
        log.info("getEventById {}", eventId);
        eventService.addStatistic(request);
        return eventService.getPublishedEventById(eventId);
    }

}
