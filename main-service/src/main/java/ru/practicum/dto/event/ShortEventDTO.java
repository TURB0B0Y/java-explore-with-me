package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.dto.category.CategoryDTO;
import ru.practicum.dto.user.UserDTO;
import ru.practicum.environment.Environments;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShortEventDTO {

    private int id;

    private String annotation;

    private CategoryDTO category;

    private int confirmedRequests;

    @JsonFormat(pattern = Environments.DATE_FORMAT)
    private LocalDateTime eventDate;

    private UserDTO initiator;

    private boolean paid;

    private String title;

    private int views;

}
