package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;
    private User user7;
    private User user8;
    private User user9;
    private User user10;
    private User user11;
    private User user12;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        userController = new UserController(new UserService(userStorage));
        user1 = new User(1L, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user2 = new User(2L, "andrey-СОБАКА-mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user3 = new User(3L, null, "andrey", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user4 = new User(4L, " ", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user5 = new User(5L, "andrey@mail.ru", null, "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user6 = new User(6L, "andrey@mail.ru", "and rey", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user7 = new User(7L, "andrey@mail.ru", "", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user8 = new User(8L, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(2025, 10, 20), null);
        user9 = new User(9L, "andrey@mail.ru", "andrey", null,
                LocalDate.of(1999, 10, 20), null);
        user10 = new User(10L, "andrey@mail.ru", "andrey", " ",
                LocalDate.of(1999, 10, 20), null);
        user11 = new User(1L, "andrey@yandex.ru", "andglb", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user12 = new User(12L, "andrey@yandex.ru", "andglb", "Andrey",
                LocalDate.of(1999, 10, 20), null);
    }
    @Test
    void createUserTest() {
        User actual = userController.create(user1);

        assertEquals(user1, actual);
    }

    @Test
    void createUserWithIncorrectEmailTest() {
        assertThrows(ValidationException.class, () -> userController.create(user2));
        assertThrows(ValidationException.class, () -> userController.create(user3));
        assertThrows(ValidationException.class, () -> userController.create(user4));
    }

    @Test
    void createUserWithIncorrectLoginTest() {
        assertThrows(ValidationException.class, () -> userController.create(user5));
        assertThrows(ValidationException.class, () -> userController.create(user6));
        assertThrows(ValidationException.class, () -> userController.create(user7));
    }

    @Test
    void createUserWithIncorrectBirthdayTest() {
        assertThrows(ValidationException.class, () -> userController.create(user8));
    }

    @Test
    void createUserWithEmptyNameTest() {
        User actual1 = userController.create(user9);
        User actual2 = userController.create(user10);

        assertEquals(user9.getLogin(), actual1.getName());
        assertEquals(user10.getLogin(), actual2.getName());
    }

    @Test
    void updateUserTest() {
        userController.create(user1);
        User actual = userController.update(user11);

        assertEquals(user11, actual);
    }

    @Test
    void updateUserWithIncorrectIdTest() {
        userController.create(user1);
        assertThrows(UserNotFoundException.class, () -> userController.update(user12));
    }

    @Test
    void getUsersTest() {
        userController.create(user1);
        userController.create(user9);
        userController.create(user10);
        userController.create(user12);

        Collection<User> actual = userController.getUsers();

        assertEquals(4, actual.size());
        assertAll("Check if all users are present",
                () -> assertTrue(actual.contains(user1)),
                () -> assertTrue(actual.contains(user9)),
                () -> assertTrue(actual.contains(user10)),
                () -> assertTrue(actual.contains(user12))
        );
    }

    @Test
    void deleteUserTest() {
        User userCreate = userController.create(user1);
        User userDelete = userController.delete(userCreate.getId());

        assertEquals(user1, userDelete);
        assertEquals(0, userController.getUsers().size());
    }

    @Test
    void deleteUserWithIncorrectIdTest() {
        assertThrows(UserNotFoundException.class, () -> userController.delete(9999L));
    }
}
