package ru.practicum.dto.location;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationDTO {

    private int id;
    private double lat;
    private double lon;

}
