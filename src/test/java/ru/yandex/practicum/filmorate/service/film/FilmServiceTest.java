package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private UserService userService;
    private UserStorage userStorage;
    private FilmService filmService;
    private FilmStorage filmStorage;
    private Film film1;
    private Film film2;
    private Film film3;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);

        film1 = new Film(1L, "Film_Name_1", "Film_Description_1",
                LocalDate.of(2021, 1, 1), 1, null);
        film2 = new Film(2L, "Film_Name_2", "Film_Description_2",
                LocalDate.of(2021, 2, 2), 2, null);
        film3 = new Film(3L, "Film_Name_3", "Film_Description_3",
                LocalDate.of(2021, 3, 3), 3, null);
        user1 = new User(1L, "andrey@mail.ru", "andrey", "Andrey",
                LocalDate.of(1999, 10, 20), null);
        user2 = new User(2L, "andrey@yandex.ru", "andrey", "Andrey",
                LocalDate.of(2000, 11, 21), null);
        user3 = new User(3L, "andrey@gmail.com", "andrey", "Andrey",
                LocalDate.of(2001, 12, 22), null);
    }
    @Test
    void addLikeTest() {
        Film firstFilm = filmService.create(film1);
        User firstUser = userService.create(user1);

        filmService.addLike(firstFilm.getId(), firstUser.getId());

        assertEquals(1, firstFilm.getLikes().size());
        assertTrue(firstFilm.getLikes().contains(firstUser.getId()));
    }

    @Test
    void addLikeWithIncorrectIdTest() {
        Film firstFilm = filmService.create(film1);
        User firstUser = userService.create(user1);

        assertThrows(UserNotFoundException.class, () -> filmService.addLike(firstFilm.getId(), 9999L));
        assertThrows(FilmNotFoundException.class, () -> filmService.addLike(9999L, firstUser.getId()));
    }
    @Test
    void deleteLikeTest() {
        Film firstFilm = filmService.create(film1);
        User firstUser = userService.create(user1);

        filmService.addLike(firstFilm.getId(), firstUser.getId());
        filmService.deleteLike(firstFilm.getId(), firstUser.getId());

        assertEquals(0, firstFilm.getLikes().size());
    }

    @Test
    void deleteLikeWithIncorrectIdTest() {
        Film firstFilm = filmService.create(film1);
        User firstUser = userService.create(user1);

        assertThrows(UserNotFoundException.class, () -> filmService.deleteLike(firstFilm.getId(), 9999L));
        assertThrows(FilmNotFoundException.class, () -> filmService.deleteLike(9999L, firstUser.getId()));
    }


    @Test
    void getPopularTest() {
        Film firstFilm = filmService.create(film1);
        Film secondFilm = filmService.create(film2);
        Film thirdFilm = filmService.create(film3);
        User firstUser = userService.create(user1);
        User secondUser = userService.create(user2);
        User thirdUser = userService.create(user3);

        filmService.addLike(firstFilm.getId(), firstUser.getId());
        filmService.addLike(firstFilm.getId(), secondUser.getId());
        filmService.addLike(firstFilm.getId(), thirdFilm.getId());
        filmService.addLike(secondFilm.getId(), firstUser.getId());
        filmService.addLike(secondFilm.getId(), thirdUser.getId());
        filmService.addLike(thirdFilm.getId(), secondUser.getId());

        List<Film> actual = new ArrayList<>();
        actual.add(firstFilm);
        actual.add(secondFilm);
        actual.add(thirdFilm);
        assertEquals(actual, filmService.getPopular(3));
    }

    @Test
    void getPopularWithIncorrectCountTest() {
        assertThrows(ValidationException.class, () -> filmService.getPopular(-1));
        assertThrows(ValidationException.class, () -> filmService.getPopular(0));
    }

    @Test
    void getFilmById() {
        Film film = filmService.create(film1);

        assertEquals(film1, filmService.getFilmById(film.getId()));
    }

    @Test
    void getFilmWithIncorrectId() {
        assertThrows(FilmNotFoundException.class, () -> filmService.getFilmById(9999L));
    }
}