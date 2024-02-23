package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CreateHitDTO;
import ru.practicum.model.Hit;

@UtilityClass
public class HitMapper {

    public static Hit fromHitDto(CreateHitDTO hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .created(hitDto.getTimestamp())
                .build();
    }

}
