package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {
    private UserService userService;
    private UserStorage userStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);

        user1 = new User(1L, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user2 = new User(2L, "andrey@yandex.ru", "andrey", "Andrey",
                LocalDate.of(2000, 11, 21), null);
        user3 = new User(3L, "andrey@gmail.com", "andrey", "Andrey",
                LocalDate.of(2001, 12, 22), null);
    }

    @Test
    void addFriendTest() {
        User firstUser = userService.create(user1);
        User secondUser = userService.create(user2);

        userService.addFriend(firstUser.getId(), secondUser.getId());

        assertEquals(1, firstUser.getFriends().size());
        assertEquals(1, secondUser.getFriends().size());
    }

    @Test
    void addFriendWithIncorrectIdTest() {
        User firstUser = userService.create(user1);

        assertThrows(UserNotFoundException.class, () -> userService.addFriend(firstUser.getId(), 3L));
    }

    @Test
    void deleteFriendTest() {
        User firstUser = userService.create(user1);
        User secondUser = userService.create(user2);

        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.deleteFriend(firstUser.getId(), secondUser.getId());

        assertEquals(0, firstUser.getFriends().size());
        assertEquals(0, secondUser.getFriends().size());
    }

    @Test
    void deleteFriendWithIncorrectIdTest() {
        User firstUser = userService.create(user1);

        assertThrows(UserNotFoundException.class, () -> userService.deleteFriend(firstUser.getId(), 999L));
    }

    @Test
    void getFriendsTest() {
        User firstUser = userService.create(user1);
        User secondUser = userService.create(user2);
        User thirdUser = userService.create(user3);

        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());

        assertEquals(2, firstUser.getFriends().size());
        assertEquals(1, secondUser.getFriends().size());
        assertEquals(1, thirdUser.getFriends().size());
        assertAll("Check if all friends are present",
                () -> assertTrue(firstUser.getFriends().contains(secondUser.getId())),
                () -> assertTrue(firstUser.getFriends().contains(thirdUser.getId())),
                () -> assertTrue(secondUser.getFriends().contains(firstUser.getId())),
                () -> assertTrue(thirdUser.getFriends().contains(firstUser.getId()))
        );
    }

    @Test
    void getFriendsWithIncorrectIdTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getFriends(999L));
    }

    @Test
    void getCommonFriendsTest() {
        User firstUser = userService.create(user1);
        User secondUser = userService.create(user2);
        User thirdUser = userService.create(user3);
        List<User> commonFriends = new ArrayList<>();
        commonFriends.add(user3);

        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.addFriend(secondUser.getId(), thirdUser.getId());

        assertEquals(commonFriends, userService.getCommonFriends(firstUser.getId(), secondUser.getId()));
    }

    @Test
    void getCommonFriendsWithIncorrectIdTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getCommonFriends(1L, 9999L));
    }

    @Test
    void getUserByIdTest() {
        User firstUser = userService.create(user1);

        assertEquals(user1, userService.getUserById(user1.getId()));
    }

    @Test
    void getUserWithIncorrectIdTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(9999L));
    }
}