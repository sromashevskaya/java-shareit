package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        validateUniqueEmail(userDto.getEmail());

        User newUser = UserMapper.toUser(userDto);
        User addedUser = userRepository.save(newUser);

        log.info("Создан новый пользователь с id={} и email={}", addedUser.getId(), addedUser.getEmail());
        return UserMapper.toUserDto(addedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = getUserOrThrow(userId);

        validateUniqueEmailExcludingUser(userDto.getEmail(), existingUser.getId());

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        User result = userRepository.save(existingUser);

        log.info("Обновлён пользователь с id={}", userId);
        return UserMapper.toUserDto(result);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = getUserOrThrow(userId);
        log.info("Пользователь с id={} найден: {}", userId, user.getEmail());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        getUserOrThrow(userId);
        userRepository.deleteById(userId);
        log.info("Удалён пользователь с id={}", userId);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ValidationException("Пользователь с email " + email + " уже существует");
        }
    }

    private void validateUniqueEmailExcludingUser(String email, Long excludeUserId) {
        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(email);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(excludeUserId)) {
            throw new ValidationException("Пользователь с email " + email + " уже существует");
        }
    }
}