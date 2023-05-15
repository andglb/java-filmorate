package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films;
    private Long filmId;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
        filmId = 0L;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Ошибка валидации! Имя не может быть пустым!");
        }
        if (film.getDescription().length() > 200 || film.getDescription().isEmpty()) {
            throw new ValidationException("Ошибка валидации! Описание должно содержать от 1 до 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка валидации! Дата релиза должна быть не раньше 28.12.1895!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Ошибка валидации! Продолжительность должна быть больше нуля!");
        }
        film.setId(++filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        checkingFilmForEmptiness(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        checkingFilmForEmptiness(filmId);
        return films.get(filmId);
    }

    @Override
    public Film delete(Long filmId) {
        checkingFilmForEmptiness(filmId);
        return films.remove(filmId);
    }

    public void checkingFilmForEmptiness(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с ID - " + filmId + " не найден!");
        }
    }
}
