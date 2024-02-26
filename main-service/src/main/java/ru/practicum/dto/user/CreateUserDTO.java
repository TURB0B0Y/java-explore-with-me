package ru.practicum.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUserDTO {

    @Email
    @NotNull @NotBlank
    @Size(min = 6, max = 254)
    private String email;

    @NotNull @NotBlank
    @Size(min = 2, max = 250)
    private String name;

}
