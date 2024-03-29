package ru.practicum.dto.compilation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCompilationDTO {

    @UniqueElements
    private List<Integer> events = Collections.emptyList();

    private Boolean pinned = false;

    @NotNull @NotBlank
    @Size(min = 1, max = 50)
    private String title;

}
