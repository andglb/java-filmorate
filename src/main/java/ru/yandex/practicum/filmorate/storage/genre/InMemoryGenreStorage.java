package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryGenreStorage")
public class InMemoryGenreStorage implements GenreStorage {
    private GenreDbStorage genreDbStorage;

    @Autowired
    public InMemoryGenreStorage(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Genre> getGenres() {
        return genreDbStorage.getGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genreDbStorage.getGenreById(id);
    }

    @Override
    public Set<Genre> getFilmGenres(Long filmId) {
        return new HashSet<>(genreDbStorage.getFilmGenres(filmId));
    }

    public void putGenres(Film film) {
        genreDbStorage.delete(film);
        genreDbStorage.add(film);
    }
}
