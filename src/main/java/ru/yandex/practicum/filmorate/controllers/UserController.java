package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Integer id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")
                || user.getLogin().isEmpty() || user.getLogin().contains(" ")
                || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка валидации!");
        }

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Получен POST-запрос к эндпоинту: '/users' на добавление пользователя");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с ID - " + user.getId() + " не найден!");
        }
        users.put(user.getId(), user);
        log.info("Получен PUT-запрос к эндпоинту: '/users' на обновление пользователя с ID - {}", user.getId());
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }
}