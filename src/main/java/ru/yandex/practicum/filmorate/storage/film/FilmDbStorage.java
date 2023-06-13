package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.InMemoryGenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.InMemoryMpaStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private InMemoryMpaStorage inMemoryMpaStorage;
    private InMemoryGenreStorage inMemoryGenreStorage;
    private LikeDbStorage likeDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, InMemoryMpaStorage inMemoryMpaStorage,
                         InMemoryGenreStorage inMemoryGenreStorage, LikeDbStorage likeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.inMemoryMpaStorage = inMemoryMpaStorage;
        this.inMemoryGenreStorage = inMemoryGenreStorage;
        this.likeDbStorage = likeDbStorage;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(likeDbStorage.getLikes(rs.getLong("id"))),
                inMemoryMpaStorage.getMpaById(rs.getInt("rating_id")),
                inMemoryGenreStorage.getFilmGenres(rs.getLong("id")))
        );

    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        film.setMpa(inMemoryMpaStorage.getMpaById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genre.setName(inMemoryGenreStorage.getGenreById(genre.getId()).getName());
            }
            inMemoryGenreStorage.putGenres(film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 0) {
            film.setMpa(inMemoryMpaStorage.getMpaById(film.getMpa().getId()));
            if (film.getGenres() != null) {
                Collection<Genre> sortGenres = film.getGenres().stream()
                        .sorted(Comparator.comparing(Genre::getId))
                        .collect(Collectors.toList());
                film.setGenres(new LinkedHashSet<>(sortGenres));
                for (Genre genre : film.getGenres()) {
                    genre.setName(inMemoryGenreStorage.getGenreById(genre.getId()).getName());
                }
            }
            inMemoryGenreStorage.putGenres(film);
            return film;
        } else {
            throw new FilmNotFoundException("Фильм с ID - " + film.getId() + " не найден!");
        }
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Film film;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", filmId);
        if (filmRows.first()) {
            Mpa mpa = inMemoryMpaStorage.getMpaById(filmRows.getInt("rating_id"));
            Set<Genre> genres = inMemoryGenreStorage.getFilmGenres(filmId);
            film = new Film(
                    filmRows.getLong("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    new HashSet<>(likeDbStorage.getLikes(filmRows.getLong("id"))),
                    mpa,
                    genres);
        } else {
            throw new FilmNotFoundException("Фильм с ID - " + filmId + " не найден!");
        }
        return film;
    }

    @Override
    public Film delete(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Film film = getFilmById(filmId);
        String sqlQuery = "DELETE FROM films WHERE id = ? ";
        if (jdbcTemplate.update(sqlQuery, filmId) == 0) {
            throw new FilmNotFoundException("Фильм с ID - " + filmId + " не найден!");
        }
        return film;
    }
}


