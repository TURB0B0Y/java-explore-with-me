package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.dto.event.ShortEventDTO;
import ru.practicum.dto.user.UserDTO;
import ru.practicum.environment.Environments;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Integer id;
    private String text;
    private UserDTO author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Environments.DATE_FORMAT)
    private LocalDateTime createDate;
    private ShortEventDTO event;

}
