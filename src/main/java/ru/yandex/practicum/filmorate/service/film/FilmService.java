package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private LikeStorage likeStorage;

@Autowired
public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                   @Qualifier("userDbStorage") UserStorage userStorage,
                   LikeStorage likeStorage) {
    this.filmStorage = filmStorage;
    this.userStorage = userStorage;
    this.likeStorage = likeStorage;
}

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (userStorage.getUserById(userId) != null) {
                likeStorage.addLike(filmId, userId);
            } else {
                throw new UserNotFoundException("Пользователь c ID - " + userId + " не найден!");
            }
        } else {
            throw new FilmNotFoundException("Фильм c ID - " + filmId + " не найден!");
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (film.getLikes().contains(userId)) {
                likeStorage.deleteLike(filmId, userId);
            } else {
                throw new UserNotFoundException("Лайк от пользователя c ID - " + userId + " не найден!");
            }
        } else {
            throw new FilmNotFoundException("Фильм c ID - " + filmId + " не найден!");
        }
    }

    public List<Film> getPopular(Integer count) {
        if (count < 1) {
            new ValidationException("Количество фильмов для вывода не должно быть меньше 1");
        }
        return likeStorage.getPopular(count);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

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
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteFilmById(Long filmId) {
        return filmStorage.delete(filmId);
    }
}
