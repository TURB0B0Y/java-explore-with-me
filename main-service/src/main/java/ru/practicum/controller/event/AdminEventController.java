package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDTO;
import ru.practicum.dto.event.UpdateEventDTO;
import ru.practicum.filter.AdminEventFilter;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventDTO> getEvents(@Valid AdminEventFilter filter) {
        log.info("getEvents {}", filter);
        return eventService.findEvents(filter);
    }

    @PatchMapping("/{eventId}")
    public EventDTO editEventById(@PathVariable int eventId, @RequestBody @Valid UpdateEventDTO dto) {
        log.info("editEventById {} {}", eventId, dto);
        return eventService.updateAdminEvent(eventId, dto);
    }

}
