package ru.practicum.dto.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateRequestResultDTO {

    @Singular
    private List<RequestDTO> confirmedRequests;

    @Singular
    private List<RequestDTO> rejectedRequests;

}
