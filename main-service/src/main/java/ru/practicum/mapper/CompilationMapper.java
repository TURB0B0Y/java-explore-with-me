package ru.practicum.mapper;

import ru.practicum.dto.compilation.CompilationDTO;
import ru.practicum.model.Compilation;

import java.util.Map;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDTO toDto(Compilation compilation, Map<Integer, Integer> views) {
        CompilationDTO dto = new CompilationDTO();
        dto.setId(compilation.getId());
        dto.setEvents(compilation.getEvents().stream().map(e -> EventMapper.toShortDto(e, views.getOrDefault(e.getId(), 0))).collect(Collectors.toList()));
        dto.setPinned(compilation.isPinned());
        dto.setTitle(compilation.getTitle());
        return dto;
    }

}
