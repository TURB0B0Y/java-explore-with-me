package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDTO {

    private String app;
    private String uri;
    private Integer hits;

}
