package ru.practicum.service;

import ru.practicum.dto.CreateHitDTO;
import ru.practicum.dto.StatisticDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    void createHit(CreateHitDTO hitDto);

    List<StatisticDTO> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);

}
