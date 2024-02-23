package ru.practicum.mapper;

import ru.practicum.dto.event.CreateEventDTO;
import ru.practicum.dto.event.EventDTO;
import ru.practicum.dto.event.ShortEventDTO;
import ru.practicum.model.Event;

public class EventMapper {

    public static Event toModel(CreateEventDTO dto) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(LocationMapper.fromDto(dto.getLocation()));
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());
        return event;
    }

    public static EventDTO toDto(Event event, int views, int confirmedRequests) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setCreatedOn(event.getCreateDate());
        dto.setInitiator(UserMapper.toDto(event.getInitiator()));
        dto.setLocation(LocationMapper.toModel(event.getLocation()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setTitle(event.getTitle());
        dto.setState(event.getState());
        dto.setViews(views);
        dto.setPublishedOn(event.getPublishedDate());
        dto.setConfirmedRequests(confirmedRequests);
        return dto;
    }

    public static ShortEventDTO toShortDto(Event event) {
        ShortEventDTO dto = new ShortEventDTO();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(UserMapper.toDto(event.getInitiator()));
        dto.setPaid(event.getPaid());
        dto.setTitle(event.getTitle());
        return dto;
    }

}
