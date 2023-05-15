package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users;
    private Long userId;
    public InMemoryUserStorage() {
        users = new HashMap<>();
        userId = 0L;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Ошибка валидации! Email не может быть пустым!");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Ошибка валидации! Email должен содержать символ '@'!");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Ошибка валидации! Логин не может быть пустым!");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка валидации! Логин не должен содержать пробелы!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка валидации! День рождения не может быть в будущем!");
        }

        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        checkingUserForEmptiness(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        checkingUserForEmptiness(userId);
        return users.get(userId);
    }

    public void checkingUserForEmptiness(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID=" + id + " не найден!");
        }
    }
}
