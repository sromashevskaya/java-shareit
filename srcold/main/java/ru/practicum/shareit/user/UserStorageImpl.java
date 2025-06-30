package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private long id = 1L;

    @Override
    public Collection<User> findAllUsers() {
        return userStorage.values();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(userStorage.get(userId));
    }

    @Override
    public User addUser(User user) {
        user.setId(id++);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NoSuchElementException("Объект не найден: " + userId);
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.remove(userId);
    }
}
