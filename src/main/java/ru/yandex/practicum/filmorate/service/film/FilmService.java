package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (!film.getLikes().contains(userId)) {
            throw new UserNotFoundException("Пользователь c ID - " + userId + " не ставил лайк к фильму с ID - " +
                    filmId + "!");
        } else {
            film.getLikes().remove(userId);
            filmStorage.update(film);
        }
    }

    public List<Film> getPopular(Integer count) {
        if (count < 1) {
            throw new ValidationException("Количество фильмов не может быть меньше 1!");
        }
/*        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingLong(film -> (long) film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());*/
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
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
