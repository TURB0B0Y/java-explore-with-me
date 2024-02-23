package ru.practicum.mapper;

import ru.practicum.dto.compilation.CompilationDTO;
import ru.practicum.model.Compilation;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDTO toDto(Compilation compilation) {
        CompilationDTO dto = new CompilationDTO();
        dto.setId(compilation.getId());
        dto.setEvents(compilation.getEvents().stream().map(EventMapper::toShortDto).collect(Collectors.toList()));
        dto.setPinned(compilation.isPinned());
        dto.setTitle(compilation.getTitle());
        return dto;
    }

}
