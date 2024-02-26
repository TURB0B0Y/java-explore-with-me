package ru.practicum.dto.location;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateLocationDTO {

    private double lat;
    private double lon;

}
