package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController = new FilmController();

    @Test
    void createFilmTest() {
        Film film = new Film(1, "Film_Name", "Film_Description",
                LocalDate.of(2021, 1, 1), 1);
        Film actual = filmController.create(film);

        assertEquals(film, actual);
    }

    @Test
    void createFilmWithEmptyNameTest() {
        Film film1 = new Film(1, null, "Film_Description_1",
                LocalDate.of(2021, 1, 1), 1);
        Film film2 = new Film(2, " ", "Film_Description_2",
                LocalDate.of(2021, 2, 2), 2);

        assertThrows(ValidationException.class, () -> filmController.create(film1));
        assertThrows(ValidationException.class, () -> filmController.create(film2));
    }

    @Test
    void createFilmWithDateBeforeTheSetDateTest() {
        Film film = new Film(1, "Film_Name", "Film_Description",
                LocalDate.of(1894, 1, 1), 1);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void filmWithDescriptionOfMoreThan200Characters() {
        Film film = new Film(1, "Film_Name", "Этот текст придуман специально для тестирования + " +
                "случая, когда в описании фильма написано больше двухсот символов. По условиям технического задания +" +
                "в таком случае этот тест должен завершиться выбросом исключения. Спасибо за внимание :)",
                LocalDate.of(2021, 1, 1), 1);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void createFilmWithNegativeDurationTest() {
        Film film = new Film(1, "Film_Name", "Film_Description",
                LocalDate.of(2021, 1, 1), -1);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void updateFilmTest() {
        Film film1 = new Film(1, "Film_Name_1", "Film_Description_1",
                LocalDate.of(2021, 1, 1), 1);
        Film film2 = new Film(1, "Film_Name_2", "Film_Description_2",
                LocalDate.of(2022, 2, 2), 2);

        filmController.create(film1);
        Film actual = filmController.update(film2);

        assertEquals(film2, actual);
    }

    @Test
    void getFilmsTest() {
        Film film1 = new Film(1, "Film_Name_1", "Film_Description_1",
                LocalDate.of(2021, 1, 1), 1);
        Film film2 = new Film(2, "Film_Name_2", "Film_Description_2",
                LocalDate.of(2021, 2, 2), 2);
        Film film3 = new Film(3, "Film_Name_3", "Film_Description_3",
                LocalDate.of(2021, 3, 3), 3);
        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        Collection<Film> actual = filmController.getFilms();

        assertEquals(3, actual.size());
        assertTrue(actual.contains(film1));
        assertTrue(actual.contains(film2));
        assertTrue(actual.contains(film3));
    }
}