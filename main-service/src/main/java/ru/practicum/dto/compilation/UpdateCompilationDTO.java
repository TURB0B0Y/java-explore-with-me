package ru.practicum.dto.compilation;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCompilationDTO {

    @UniqueElements
    private List<Integer> events = Collections.emptyList();

    private Boolean pinned = false;

    @Size(min = 1, max = 50)
    private String title;

}
