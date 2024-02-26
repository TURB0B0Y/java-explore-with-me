package ru.practicum.dto.request;

import lombok.*;
import ru.practicum.enums.StatusRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateRequestDTO {

    private List<Integer> requestIds;

    @NotNull
    private StatusRequest status;

}
