package ru.practicum.service;

import ru.practicum.dto.event.CreateEventDTO;
import ru.practicum.dto.event.EventDTO;
import ru.practicum.dto.event.ShortEventDTO;
import ru.practicum.dto.event.UpdateEventDTO;
import ru.practicum.filter.AdminEventFilter;
import ru.practicum.filter.EventFilter;
import ru.practicum.filter.PageFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventDTO> findEvents(AdminEventFilter filter);

    EventDTO updateAdminEvent(int eventId, UpdateEventDTO dto);

    void addStatistic(HttpServletRequest request);

    EventDTO getPublishedEventById(int eventId);

    List<ShortEventDTO> findEvents(EventFilter filter);

    List<ShortEventDTO> getAll(int userId, PageFilter pageFilter);

    EventDTO createEvent(int userId, CreateEventDTO dto);

    EventDTO getByInitiatorAndId(int userId, int eventId);

    EventDTO updateEvent(int userId, int eventId, UpdateEventDTO dto);
}
