package ru.practicum.service;

import ru.practicum.dto.user.CreateUserDTO;
import ru.practicum.dto.user.UserDTO;
import ru.practicum.filter.PageFilter;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsers(PageFilter pageFilter);

    List<UserDTO> getUsers(List<Integer> ids);

    UserDTO createUser(CreateUserDTO dto);

    void deleteUserById(int userId);

}
