package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film1;
    private Film film2;
    private Film film3;
    private Film film4;
    private Film film5;
    private Film film6;
    private Film film7;
    private Film film8;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film1 = new Film(1, "Film_Name_1", "Film_Description_1",
                LocalDate.of(2021, 1, 1), 1);
        film2 = new Film(2, null, "Film_Description_2",
                LocalDate.of(2021, 2, 2), 2);
        film3 = new Film(3, " ", "Film_Description_3",
                LocalDate.of(2021, 3, 3), 3);
        film4 = new Film(4, "Film_Name_4", "Film_Description_4",
                LocalDate.of(1894, 4, 4), 4);
        film5 = new Film(5, "Film_Name_5", "Этот текст придуман специально для тестирования " +
                "случая, когда в описании фильма написано больше двухсот символов. По условиям технического задания " +
                "в таком случае этот тест должен завершиться выбросом исключения. Спасибо за внимание :)",
                LocalDate.of(2021, 5, 5), 5);
        film6 = new Film(6, "Film_Name_6", "Film_Description_6",
                LocalDate.of(2021, 6, 6), -6);
        film7 = new Film(1, "Film_Name_7", "Film_Description_7",
                LocalDate.of(2021, 7, 7), 7);
        film8 = new Film(8, "Film_Name_8", "Film_Description_8",
                LocalDate.of(2021, 8, 8), 8);
    }

    @Test
    void createFilmTest() {
        Film actual = filmController.create(film1);

        assertEquals(film1, actual);
    }

    @Test
    void createFilmWithEmptyNameTest() {
        assertThrows(ValidationException.class, () -> filmController.create(film2));
        assertThrows(ValidationException.class, () -> filmController.create(film3));
    }

    @Test
    void createFilmWithDateBeforeTheSetDateTest() {
        assertThrows(ValidationException.class, () -> filmController.create(film4));
    }

    @Test
    void filmWithDescriptionOfMoreThan200Characters() {
        assertThrows(ValidationException.class, () -> filmController.create(film5));
    }

    @Test
    void createFilmWithNegativeDurationTest() {
        assertThrows(ValidationException.class, () -> filmController.create(film6));
    }

    @Test
    void updateFilmTest() {
        filmController.create(film1);
        Film actual = filmController.update(film7);

        assertEquals(film7, actual);
    }

    @Test
    void updateFilmWithIncorrectIdTest() {
        filmController.create(film1);

        assertThrows(ValidationException.class, () -> filmController.update(film8));
    }

    @Test
    void getFilmsTest() {
        filmController.create(film1);
        filmController.create(film8);

        Collection<Film> actual = filmController.getFilms();

        assertEquals(2, actual.size());
        assertAll("Check if all films are present",
                () -> assertTrue(actual.contains(film1)),
                () -> assertTrue(actual.contains(film8))
        );
    }
}