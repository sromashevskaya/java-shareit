package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto findUserById(Long userId);

    void deleteUser(Long userId);
}
