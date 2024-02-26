package ru.practicum.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageFilter {

    @Min(0)
    private Integer from = 0;

    @Min(1)
    private Integer size = 10;

}
