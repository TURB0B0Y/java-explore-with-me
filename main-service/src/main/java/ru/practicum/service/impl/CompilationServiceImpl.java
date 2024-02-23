package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatisticClient;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.dto.compilation.CompilationDTO;
import ru.practicum.dto.compilation.CreateCompilationDTO;
import ru.practicum.dto.compilation.UpdateCompilationDTO;
import ru.practicum.environment.Environments;
import ru.practicum.exception.NotFoundException;
import ru.practicum.filter.PageFilter;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern(Environments.DATE_FORMAT);

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatisticClient statisticClient;

    @Override
    @Transactional
    public CompilationDTO createCompilation(CreateCompilationDTO dto) {
        List<Event> events = dto.getEvents().isEmpty()
                ? Collections.emptyList()
                : findAllEventByIds(dto.getEvents());
        Compilation newCompilation = new Compilation();
        newCompilation.setTitle(dto.getTitle());
        newCompilation.setPinned(dto.getPinned());
        newCompilation.setEvents(events);
        Map<Integer, Integer> views = getViews(events);
        return CompilationMapper.toDto(compilationRepository.save(newCompilation), views);
    }

    @Override
    @Transactional
    public void deleteCompilation(int compilationId) {
        Compilation compilation = getById(compilationId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDTO updateCompilation(int compilationId, UpdateCompilationDTO dto) {
        Compilation compilationFromDB = getById(compilationId);
        List<Event> events = dto.getEvents().isEmpty()
                ? Collections.emptyList() : findAllEventByIds(dto.getEvents());
        compilationFromDB.setEvents(events);
        compilationFromDB.setTitle(dto.getTitle());
        compilationFromDB.setPinned(dto.getPinned());
        Map<Integer, Integer> views = getViews(events);
        return CompilationMapper.toDto(compilationFromDB, views);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDTO> getCompilations(Boolean pinned, PageFilter pageFilter) {
        Pageable pageable = PageRequest.of(pageFilter.getFrom() / pageFilter.getSize(), pageFilter.getSize());
        List<Compilation> res;
        if (Objects.nonNull(pinned))
            res = compilationRepository.findAllByPinned(pinned, pageable);
        else
            res = compilationRepository.findAll(pageable).getContent();
        List<Event> events = res.stream().flatMap(c -> c.getEvents()
                .stream())
                .collect(Collectors.toList());
        Map<Integer, Integer> views = getViews(events);
        return res.stream().map(c -> CompilationMapper.toDto(c, views))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDTO getCompilationById(int compilationId) {
        Compilation compilation = getById(compilationId);
        Map<Integer, Integer> views = getViews(compilation.getEvents());
        return CompilationMapper.toDto(compilation, views);
    }

    private List<Event> findAllEventByIds(List<Integer> eventIds) {
        return eventRepository.findAllById(eventIds);
    }

    private Compilation getById(int compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=%s was not found", compilationId));
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

}
