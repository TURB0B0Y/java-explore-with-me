package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDTO;
import ru.practicum.filter.PageFilter;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDTO> getCompilations(
            @Valid PageFilter pageFilter,
            @RequestParam(required = false) Boolean pinned
    ) {
        log.info("getCompilations {} {}", pinned, pageFilter);
        return compilationService.getCompilations(pinned, pageFilter);
    }

    @GetMapping("/{compilationId}")
    public CompilationDTO getCompilationById(@PathVariable int compilationId) {
        log.info("getCompilationById {}", compilationId);
        return compilationService.getCompilationById(compilationId);
    }

}
