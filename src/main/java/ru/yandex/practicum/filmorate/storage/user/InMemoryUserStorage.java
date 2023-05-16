package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

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
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        ensureUserExists(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        ensureUserExists(userId);
        return users.get(userId);
    }

    @Override
    public User delete(Long userId) {
        ensureUserExists(userId);
        for (User user : users.values()) {
            user.getFriends().remove(userId);
        }
        return users.remove(userId);
    }

    public void ensureUserExists(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID - " + id + " не найден!");
        }
    }
}
