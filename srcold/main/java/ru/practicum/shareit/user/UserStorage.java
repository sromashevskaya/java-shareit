package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User addUser(User newUser);

    User updateUser(Long userId, UserDto userDto);

    Collection<User> findAllUsers();

    Optional<User> findUserById(Long userId);

    void deleteUser(Long userId);
}
