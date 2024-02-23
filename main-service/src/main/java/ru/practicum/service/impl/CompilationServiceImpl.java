package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDTO;
import ru.practicum.dto.compilation.CreateCompilationDTO;
import ru.practicum.dto.compilation.UpdateCompilationDTO;
import ru.practicum.exception.NotFoundException;
import ru.practicum.filter.PageFilter;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

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
        return CompilationMapper.toDto(compilationRepository.save(newCompilation));
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
        return CompilationMapper.toDto(compilationFromDB);
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
        return res.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDTO getCompilationById(int compilationId) {
        return CompilationMapper.toDto(getById(compilationId));
    }

    private List<Event> findAllEventByIds(List<Integer> eventIds) {
        return eventRepository.findAllById(eventIds);
    }

    private Compilation getById(int compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=%s was not found", compilationId));
    }

}
