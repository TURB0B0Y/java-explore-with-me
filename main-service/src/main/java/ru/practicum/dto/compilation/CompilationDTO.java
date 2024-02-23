package ru.practicum.dto.compilation;

import lombok.*;
import ru.practicum.dto.event.ShortEventDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompilationDTO {

    private int id;
    private List<ShortEventDTO> events;
    private boolean pinned;
    private String title;

}
