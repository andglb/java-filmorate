package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Integer id = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()
                || film.getDescription().length() > 200 || film.getDescription().isEmpty()
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || film.getDuration() <= 0) {
            throw new ValidationException("Ошибка валидации!");
        }
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Получен POST-запрос к эндпоинту: '/films' на добавление фильма");
        return film;

    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с ID - " + film.getId() + " не найден!");
        }
        films.put(film.getId(), film);
        log.info("Получен PUT-запрос к эндпоинту: '/films' на обновление фильма с ID - {}", film.getId());
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }
}
