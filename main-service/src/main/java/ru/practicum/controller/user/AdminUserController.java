package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.CreateUserDTO;
import ru.practicum.dto.user.UserDTO;
import ru.practicum.filter.PageFilter;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getUsers(PageFilter pageFilter, @RequestParam(required = false) List<Integer> ids) {
        log.info("getUsers {} {}", ids, pageFilter);
        return ids == null || ids.isEmpty() ? userService.getUsers(pageFilter) : userService.getUsers(ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserDTO dto) {
        log.info("createUser {}", dto);
        return userService.createUser(dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        log.info("deleteUser {}", userId);
        userService.deleteUserById(userId);
    }

}
