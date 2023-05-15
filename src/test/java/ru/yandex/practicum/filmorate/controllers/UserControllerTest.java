/*
package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
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
        userController = new UserController();
        user1 = new User(1, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));
        user2 = new User(2, "andrey-СОБАКА-mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));
        user3 = new User(3, null, "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));
        user4 = new User(4, " ", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));
        user5 = new User(5, "andrey@mail.ru", null, "Andrey",
                LocalDate.of(1999, 10, 20));
        user6 = new User(6, "andrey@mail.ru", "and rey", "Andrey",
                LocalDate.of(1999, 10, 20));
        user7 = new User(7, "andrey@mail.ru", "", "Andrey",
                LocalDate.of(1999, 10, 20));
        user8 = new User(8, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(2025, 10, 20));
        user9 = new User(9, "andrey@mail.ru", "andrey", null,
                LocalDate.of(1999, 10, 20));
        user10 = new User(10, "andrey@mail.ru", "andrey", " ",
                LocalDate.of(1999, 10, 20));
        user11 = new User(1, "andrey@yandex.ru", "andglb", "Andrey",
                LocalDate.of(1999, 10, 20));
        user12 = new User(12, "andrey@yandex.ru", "andglb", "Andrey",
                LocalDate.of(1999, 10, 20));
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
        assertThrows(ValidationException.class, () -> userController.update(user12));
    }

    @Test
    void getUsers() {

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
}*/
