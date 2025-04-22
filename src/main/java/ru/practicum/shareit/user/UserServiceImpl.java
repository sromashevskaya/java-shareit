package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        validateUniqueEmail(userDto.getEmail());

        User newUser = userMapper.toUser(userDto);
        User addedUser = userStorage.addUser(newUser);

        log.info("Создан новый пользователь с id={} и email={}", addedUser.getId(), addedUser.getEmail());
        return userMapper.toUserDto(addedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = getUserOrThrow(userId);

        validateUniqueEmailExcludingUser(userDto.getEmail(), existingUser.getId());

        User result = userStorage.updateUser(userId, userDto);

        log.info("Обновлён пользователь с id={}", userId);
        return userMapper.toUserDto(result);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = getUserOrThrow(userId);
        log.info("Пользователь с id={} найден: {}", userId, user.getEmail());
        return userMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        getUserOrThrow(userId);
        userStorage.deleteUser(userId);
        log.info("Удалён пользователь с id={}", userId);
    }

    private User getUserOrThrow(Long userId) {
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private void validateUniqueEmail(String email) {
        for (User user : userStorage.findAllUsers()) {
            if (user.getEmail() != null && user.getEmail().equals(email)) {
                throw new ValidationException("Пользователь с email " + email + " уже существует");
            }
        }
    }

    private void validateUniqueEmailExcludingUser(String email, Long excludeUserId) {
        boolean emailExists = userStorage.findAllUsers().stream()
                .filter(user -> !user.getId().equals(excludeUserId))
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));

        if (emailExists) {
            throw new ValidationException("Пользователь с email " + email + " уже существует");
        }
    }
}
