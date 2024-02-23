package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CreateHitDTO;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.exception.BadRequestException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.mapper.StatisticViewMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.StatisticView;
import ru.practicum.repository.HitRepository;
import ru.practicum.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void createHit(CreateHitDTO hitDto) {
        Hit endpointHit = HitMapper.fromHitDto(hitDto);
        hitRepository.save(endpointHit);
    }

    @Override
    public List<StatisticDTO> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        validateDateRange(start, end);
        Sort sort = Sort.by(Sort.Direction.DESC, "hits");
        List<StatisticView> result = unique ?
                getStatsUnique(start, end, uris, sort) :
                getAll(start, end, uris, sort);
        return result.stream()
                .map(StatisticViewMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }


    private List<StatisticView> getStatsUnique(LocalDateTime start, LocalDateTime end, String[] uris, Sort sort) {
        return (uris == null) ?
                hitRepository.getStatsUnique(start, end, sort) :
                hitRepository.getStatsUnique(start, end, uris, sort);
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            log.error("Дата окончания не может быть раньше даты начала");
            throw new BadRequestException("Дата окончания не может быть раньше даты начала");
        }
    }

    private List<StatisticView> getAll(LocalDateTime start, LocalDateTime end, String[] uris, Sort sort) {
        return (uris == null) ?
                hitRepository.getAll(start, end, sort) :
                hitRepository.getAll(start, end, uris, sort);
    }

}
