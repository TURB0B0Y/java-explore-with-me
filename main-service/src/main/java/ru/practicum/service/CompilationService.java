package ru.practicum.service;

import ru.practicum.dto.compilation.CompilationDTO;
import ru.practicum.dto.compilation.CreateCompilationDTO;
import ru.practicum.dto.compilation.UpdateCompilationDTO;
import ru.practicum.filter.PageFilter;

import java.util.List;

public interface CompilationService {

    CompilationDTO createCompilation(CreateCompilationDTO dto);

    void deleteCompilation(int compilationId);

    CompilationDTO updateCompilation(int compilationId, UpdateCompilationDTO dto);

    List<CompilationDTO> getCompilations(Boolean pinned, PageFilter pageFilter);

    CompilationDTO getCompilationById(int compilationId);

}
