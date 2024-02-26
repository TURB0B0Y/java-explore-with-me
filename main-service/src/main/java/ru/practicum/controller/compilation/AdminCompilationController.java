package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDTO;
import ru.practicum.dto.compilation.CreateCompilationDTO;
import ru.practicum.dto.compilation.UpdateCompilationDTO;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDTO createCompilation(@RequestBody @Valid CreateCompilationDTO dto) {
        log.info("createCompilation {}", dto);
        return compilationService.createCompilation(dto);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compilationId) {
        log.info("deleteCompilation {}", compilationId);
        compilationService.deleteCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDTO updateCompilation(
            @PathVariable int compilationId,
            @RequestBody @Valid UpdateCompilationDTO dto
    ) {
        log.info("updateCompilation {} {}", compilationId, dto);
        return compilationService.updateCompilation(compilationId, dto);
    }

}
