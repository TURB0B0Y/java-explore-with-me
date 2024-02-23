package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.model.StatisticView;

@UtilityClass
public class StatisticViewMapper {

    public static StatisticDTO toViewStatsDto(StatisticView viewStats) {
        return StatisticDTO.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

}
