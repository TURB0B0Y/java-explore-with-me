package ru.practicum.mapper;

import ru.practicum.dto.user.UserDTO;
import ru.practicum.model.User;

public class UserMapper {

    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

}
