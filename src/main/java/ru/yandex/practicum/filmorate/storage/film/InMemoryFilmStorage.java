package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component("inMemoryFilmStorage")
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
        film.setId(++filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        ensureFilmExists(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        ensureFilmExists(filmId);
        return films.get(filmId);
    }

    @Override
    public Film delete(Long filmId) {
        ensureFilmExists(filmId);
        return films.remove(filmId);
    }

    public void ensureFilmExists(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с ID - " + id + " не найден!");
        }
    }
}