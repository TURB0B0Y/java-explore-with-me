package ru.practicum.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDTO {

    @NotBlank
    @Size(min = 5, max = 1000)
    private String text;

}
