package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController();

    @Test
    void createUserTest() {
        User user = new User(1, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));

        User actual = userController.create(user);

        assertEquals(user, actual);
    }

    @Test
    void createUserWithIncorrectEmailTest() {
        User user1 = new User(1, "andrey-СОБАКА-mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));
        User user2 = new User(1, null, "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));
        User user3 = new User(1, " ", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));

        assertThrows(ValidationException.class, () -> userController.create(user1));
        assertThrows(ValidationException.class, () -> userController.create(user2));
        assertThrows(ValidationException.class, () -> userController.create(user3));
    }

    @Test
    void createUserWithIncorrectLoginTest() {
        User user1 = new User(1, "andrey@mail.ru", null, "Andrey",
                LocalDate.of(1999, 10, 20));
        User user2 = new User(1, "andrey@mail.ru", "and rey", "Andrey",
                LocalDate.of(1999, 10, 20));
        User user3 = new User(1, "andrey@mail.ru", "", "Andrey",
                LocalDate.of(1999, 10, 20));

        assertThrows(ValidationException.class, () -> userController.create(user1));
        assertThrows(ValidationException.class, () -> userController.create(user2));
        assertThrows(ValidationException.class, () -> userController.create(user3));
    }

    @Test
    void createUserWithIncorrectBirthdayTest() {
        User user = new User(1, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(2025, 10, 20));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void createUserWithEmptyNameTest() {
        User user1 = new User(1, "andrey@mail.ru", "andrey", null,
                LocalDate.of(1999, 10, 20));
        User user2 = new User(2, "andrey@mail.ru", "andrey", " ",
                LocalDate.of(1999, 10, 20));

        User actual1 = userController.create(user1);
        User actual2 = userController.create(user2);

        assertEquals(user1.getLogin(), actual1.getName());
        assertEquals(user2.getLogin(), actual2.getName());
    }
    @Test
    void updateUserTest() {
        User user1 = new User(1, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(1998, 10, 20));
        User user2 = new User(1, "andrey@yandex.ru", "andglb", "Андрей",
                LocalDate.of(1999, 10, 20));

        userController.create(user1);
        User actual = userController.update(user2);

        assertEquals(user2, actual);
    }

    @Test
    void getUsers() {
        User user1 = new User(1, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20));
        User user2 = new User(2, "andrew_g0lub@mail.ru", "andrew_g0lub", "Эндрю",
                LocalDate.of(1999, 10, 20));
        User user3 = new User(3, "andglb@mail.ru", "andglb", "Andrew",
                LocalDate.of(1999, 10, 20));

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        Collection<User> actual = userController.getUsers();

        assertEquals(3, actual.size());
        assertTrue(actual.contains(user1));
        assertTrue(actual.contains(user2));
        assertTrue(actual.contains(user3));
    }
}