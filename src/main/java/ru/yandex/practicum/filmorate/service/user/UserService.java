package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private UserStorage userStorage;
    private FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья!");
        }
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя удалить самого себя из друзей!");
        }
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        if (userId != null) {
            friends = friendStorage.getFriends(userId);
        }
        return friends;
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {

        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        Set<User> intersection = null;

        if ((firstUser != null) && (secondUser != null)) {
            intersection = new HashSet<>(friendStorage.getFriends(firstUserId));
            intersection.retainAll(friendStorage.getFriends(secondUserId));
        }
        return new ArrayList<User>(intersection);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

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
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User deleteUserById(Long userId) {
        return userStorage.delete(userId);
    }
}

