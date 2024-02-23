package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatisticClient;
import ru.practicum.dto.CreateHitDTO;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.dto.event.CreateEventDTO;
import ru.practicum.dto.event.EventDTO;
import ru.practicum.dto.event.ShortEventDTO;
import ru.practicum.dto.event.UpdateEventDTO;
import ru.practicum.enums.EventSort;
import ru.practicum.enums.EventState;
import ru.practicum.enums.StatusRequest;
import ru.practicum.enums.UpdateEventState;
import ru.practicum.environment.Environments;
import ru.practicum.exception.APIException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.filter.AdminEventFilter;
import ru.practicum.filter.EventFilter;
import ru.practicum.filter.PageFilter;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.specification.EventSpecification;
import ru.practicum.view.EventRequestsView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern(Environments.DATE_FORMAT);
    public static final String EVENT_NOT_FOUND_TEMPLATE = "Event with id=%s was not found";

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticClient statisticClient;

    @Override
    @Transactional(readOnly = true)
    public List<EventDTO> findEvents(AdminEventFilter filter) {
        Pageable pageable = PageRequest.of(
                filter.getFrom() / filter.getSize(),
                filter.getSize()
        );
        List<Event> events = eventRepository.findAll(EventSpecification.isOnlyTheFilter(filter), pageable).getContent();
        return toDto(events);
    }

    @Override
    @Transactional
    public EventDTO updateAdminEvent(int eventId, UpdateEventDTO dto) {
        Event fromDB = getEventById(eventId);
        if (dto.getStateAction() != null && !EventState.PENDING.equals(fromDB.getState())) {
            if (UpdateEventState.PUBLISH_EVENT.equals(dto.getStateAction()))
                throw new ConflictException("Only pending events can be published");
            if (UpdateEventState.REJECT_EVENT.equals(dto.getStateAction()))
                throw new ConflictException("Only pending events can be canceled");
        }
        checkEventDate(dto.getEventDate());
        if (Objects.nonNull(dto.getStateAction())) {
            switch (dto.getStateAction()) {
                case REJECT_EVENT:
                    fromDB.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    fromDB.setState(EventState.PUBLISHED);
                    fromDB.setPublishedDate(LocalDateTime.now());
                    break;
            }
        }
        updateEvent(fromDB, dto);
        return toDto(Collections.singletonList(fromDB)).get(0);
    }

    @Override
    public void addStatistic(HttpServletRequest request) {
        CreateHitDTO dto = CreateHitDTO.builder()
                .app("ewm-main-service")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        statisticClient.addHit(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDTO getPublishedEventById(int eventId) {
        Event event = getEventById(eventId);
        if (!EventState.PUBLISHED.equals(event.getState()))
            throw new NotFoundException(EVENT_NOT_FOUND_TEMPLATE, eventId);
        return toDto(Collections.singletonList(event)).get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortEventDTO> findEvents(EventFilter eventFilter) {
        if (
                Objects.nonNull(eventFilter.getRangeStart())
                        && Objects.nonNull(eventFilter.getRangeEnd())
                        && eventFilter.getRangeStart().isAfter(eventFilter.getRangeEnd())
        ) throw new APIException("range start after range end");
        Pageable pageable;
        if (Objects.nonNull(eventFilter.getSort()) && eventFilter.getSort() == EventSort.EVENT_DATE) {
            pageable = PageRequest.of(
                    eventFilter.getFrom() / eventFilter.getSize(),
                    eventFilter.getSize(),
                    Sort.by("eventDate")
            );
        } else {
            pageable = PageRequest.of(eventFilter.getFrom() / eventFilter.getSize(), eventFilter.getSize());
        }
        List<Event> events = eventRepository.findAll(EventSpecification.isOnlyTheFilter(eventFilter), pageable).getContent();
        events = new LinkedList<>(events);
        final Map<Integer, Integer> views = getViews(events);
        if (Objects.nonNull(eventFilter.getSort()) && eventFilter.getSort().equals(EventSort.VIEWS)) {
            events.sort(Comparator.comparingInt(o -> views.getOrDefault(o.getId(), 0)));
        }
        return events.stream().map(e -> EventMapper.toShortDto(e, views.getOrDefault(e.getId(), 0))).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortEventDTO> getAll(int userId, PageFilter pageFilter) {
        Pageable pageable = PageRequest.of(pageFilter.getFrom() / pageFilter.getSize(), pageFilter.getSize());
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        Map<Integer, Integer> views = getViews(events);
        return events.stream().map(e -> EventMapper.toShortDto(e, views.getOrDefault(e.getId(), 0))).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDTO createEvent(int userId, CreateEventDTO dto) {
        checkEventDate(dto.getEventDate());
        Category category = getCategoryById(dto.getCategory());
        User initiator = getUserById(userId);
        Event event = EventMapper.toModel(dto);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        if (event.getLocation().getId() == null)
            locationRepository.save(event.getLocation());
        return EventMapper.toDto(eventRepository.save(event), 0, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDTO getByInitiatorAndId(int userId, int eventId) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_TEMPLATE, eventId)));
        return toDto(Collections.singletonList(event)).get(0);
    }

    @Override
    @Transactional
    public EventDTO updateEvent(int userId, int eventId, UpdateEventDTO dto) {
        Event fromDB = getEventById(eventId);
        if (!fromDB.getInitiator().getId().equals(userId))
            throw new ConflictException("Only owner user can update event");
        if (EventState.PUBLISHED.equals(fromDB.getState()))
            throw new ConflictException("Only pending or canceled events can be changed");
        if (Objects.nonNull(dto.getStateAction())) {
            switch (dto.getStateAction()) {
                case CANCEL_REVIEW:
                    fromDB.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    fromDB.setState(EventState.PENDING);
                    break;
            }
        }
        checkEventDate(dto.getEventDate());
        updateEvent(fromDB, dto);
        return toDto(Collections.singletonList(fromDB)).get(0);
    }

    private List<EventDTO> toDto(List<Event> events) {
        Map<Integer, Integer> views = getViews(events);
        Map<Integer, Integer> confirmedRequests = getConfirmedRequests(events);
        return events.stream().map(event -> EventMapper.toDto(
                event,
                views.getOrDefault(event.getId(), 0),
                confirmedRequests.getOrDefault(event.getId(), 0)
        )).collect(Collectors.toList());
    }

    private Map<Integer, Integer> getViews(List<Event> events) {
        String[] uris = events.stream().map(event -> "/events/" + event.getId()).toArray(String[]::new);
        List<StatisticDTO> stats = statisticClient.getStats(
                LocalDateTime.now().minusHours(5).format(DF),
                LocalDateTime.now().plusHours(5).format(DF),
                uris,
                true
        ).getBody();
        if (stats == null || stats.isEmpty())
            return Collections.emptyMap();
        Map<Integer, Integer> views = new HashMap<>();
        for (StatisticDTO state : stats) {
            String eventIdStr = state.getUri().substring(state.getUri().lastIndexOf('/') + 1);
            int eventId = Integer.parseInt(eventIdStr);
            views.put(eventId, state.getHits());
        }
        return views;
    }

    private Map<Integer, Integer> getConfirmedRequests(List<Event> events) {
        List<EventRequestsView> requestsViews = eventRepository.findAllRequestByEventAndStatus(events, StatusRequest.CONFIRMED);
        return requestsViews.stream().collect(Collectors.toMap(EventRequestsView::getEventId, EventRequestsView::getRequests));
    }

    private Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_TEMPLATE, eventId));
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new APIException("Event date most been 2 hours after now");
        }
    }

    private void updateEvent(Event fromDB, UpdateEventDTO dto) {
        if (Objects.nonNull(dto.getCategory())) {
            Category category = getCategoryById(dto.getCategory());
            fromDB.setCategory(category);
        }
        if (Objects.nonNull(dto.getAnnotation()))
            fromDB.setAnnotation(dto.getAnnotation());
        if (Objects.nonNull(dto.getDescription()))
            fromDB.setDescription(dto.getDescription());
        if (Objects.nonNull(dto.getEventDate()))
            fromDB.setEventDate(dto.getEventDate());
        if (Objects.nonNull(dto.getLocation())) {
            Location location = LocationMapper.fromDto(dto.getLocation());
            locationRepository.save(location);
            fromDB.setLocation(location);
        }
        if (Objects.nonNull(dto.getPaid()))
            fromDB.setPaid(dto.getPaid());
        if (Objects.nonNull(dto.getParticipantLimit()))
            fromDB.setParticipantLimit(dto.getParticipantLimit());
        if (Objects.nonNull(dto.getRequestModeration()))
            fromDB.setRequestModeration(dto.getRequestModeration());
        if (Objects.nonNull(dto.getTitle()))
            fromDB.setTitle(dto.getTitle());
    }

    private Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=%s was not found", categoryId));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=%s was not found", userId));
    }

}
