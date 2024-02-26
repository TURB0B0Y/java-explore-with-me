package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.CreateUserDTO;
import ru.practicum.dto.user.UserDTO;
import ru.practicum.exception.NotFoundException;
import ru.practicum.filter.PageFilter;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(PageFilter pageFilter) {
        Pageable pageable = PageRequest.of(pageFilter.getFrom() / pageFilter.getSize(), pageFilter.getSize());
        return userRepository.findAll(pageable).getContent()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(List<Integer> ids) {
        return userRepository.findAllById(ids)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUserById(int userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=%s was not found", userId));
    }

}
