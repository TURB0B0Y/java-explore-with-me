package ru.practicum.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatisticClient;

@Configuration
public class StatisticClientConfiguration {

    @Bean
    public StatisticClient statisticClient(@Value("${statistic.server.url}") String statisticServerUrl, RestTemplateBuilder builder) {
        return new StatisticClient(statisticServerUrl, builder);
    }

}
