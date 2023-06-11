package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.InMemoryGenreStorage;

import java.util.*;

@Service
public class GenreService {
    private InMemoryGenreStorage inMemoryGenreStorage;

    @Autowired
    public GenreService(InMemoryGenreStorage inMemoryGenreStorage) {
        this.inMemoryGenreStorage = inMemoryGenreStorage;
    }

    public List<Genre> getGenres() {
        return inMemoryGenreStorage.getGenres();
    }

    public Genre getGenreById(Integer id) {
        return inMemoryGenreStorage.getGenreById(id);
    }

    public void putGenres(Film film) {
        inMemoryGenreStorage.putGenres(film);
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        return inMemoryGenreStorage.getFilmGenres(filmId);
    }
}

