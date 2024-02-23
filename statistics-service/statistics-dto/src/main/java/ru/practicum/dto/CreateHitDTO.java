package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.environment.Environments;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHitDTO {

    @NotBlank(message = "IP-адрес пользователя не может быть пустым")
    private String ip;

    @NotBlank(message = "ID сервиса не может быть пустым")
    private String app;

    @NotBlank(message = "URI не может быть пустым")
    private String uri;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Environments.DATE_FORMAT)
    private LocalDateTime timestamp;

}
