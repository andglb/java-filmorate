package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "Ошибка валидации! Имя не может быть пустым!")
    private String name;
    @Size(min = 1, max = 200, message = "Ошибка валидации! Описание должно содержать от 1 до 200 символов!")
    private String description;
    @NotNull(message = "Ошибка валидации! Дата релиза не может быть пустой!")
    private LocalDate releaseDate;
    @Positive(message = "Ошибка валидации! Продолжительность должна быть больше нуля!")
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Set<Long> likes,
                Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
        if (genres == null) {
            this.genres = new HashSet<>();
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_Date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", mpa.getId());
        return values;
    }
}
