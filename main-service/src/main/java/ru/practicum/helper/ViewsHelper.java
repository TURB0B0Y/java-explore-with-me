package ru.practicum.helper;

import ru.practicum.StatisticClient;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.environment.Environments;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewsHelper {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern(Environments.DATE_FORMAT);

    public static Map<Integer, Integer> getViews(List<Event> events, StatisticClient statisticClient) {
        String[] uris = events.stream().map(event -> "/events/" + event.getId()).toArray(String[]::new);
        List<StatisticDTO> stats = statisticClient.getStats(
                LocalDateTime.now().minusHours(5).format(DF),
                LocalDateTime.now().plusHours(5).format(DF),
                uris,
                true
        ).getBody();
        if (stats == null || stats.isEmpty())
            return Collections.emptyMap();
        Map<Integer, Integer> views = new HashMap<>();
        for (StatisticDTO state : stats) {
            String eventIdStr = state.getUri().substring(state.getUri().lastIndexOf('/') + 1);
            int eventId = Integer.parseInt(eventIdStr);
            views.put(eventId, state.getHits());
        }
        return views;
    }

}
