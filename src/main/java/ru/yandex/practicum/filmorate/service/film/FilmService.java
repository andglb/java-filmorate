package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

/*
Сильно выпал из графика, из-за этого так и не доделал валидацию через аннотации. За этот месяц очень постараюсь
ускориться, чтобы всё успеть и не улететь в академ :)
*/
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм с ID = " + filmId + " не найден!");
        } else {
            if (userStorage.getUserById(userId) == null) {
                throw new UserNotFoundException("Пользователь с ID = " + userId + " не найден!");
            } else {
                film.getLikes().add(userId);
            }
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм c ID = " + filmId + " не найден!");
        } else {
            if (!film.getLikes().contains(userId)) {
                throw new UserNotFoundException("Пользователь c ID = " + userId + " не ставил лайк к фильму с ID = " +
                        filmId + "!");
            } else {
                film.getLikes().remove(userId);
            }
        }
    }

    public List<Film> getPopular(Integer count) {
        if (count < 1) {
            throw new ValidationException("Количество фильмов не может быть меньше 1!");
        }
        /* подскажите пожалуйста, почему способ который в комментарии может не работать?
        нашел его на просторах интернета
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingLong(Film::getLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());*/
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
