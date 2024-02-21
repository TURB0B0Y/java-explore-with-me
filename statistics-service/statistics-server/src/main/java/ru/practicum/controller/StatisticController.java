package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreateHitDTO;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.envirnoment.Environments;
import ru.practicum.service.StatisticService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@Valid @RequestBody CreateHitDTO hitDto) {
        log.info("createHit {}", hitDto);
        statsService.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatisticDTO> getStats(@RequestParam @DateTimeFormat(pattern = Environments.DATE_FORMAT) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = Environments.DATE_FORMAT) LocalDateTime end,
                                       @RequestParam(required = false) String[] uris,
                                       @RequestParam(defaultValue = "false") boolean unique) {
        log.info("getStats {} {} {} {}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

}
