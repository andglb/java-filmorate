package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotBlank(message = "Ошибка валидации! Имя не может быть пустым!")
    private String name;
    @Size(min = 1, max = 200, message = "Ошибка валидации! Описание должно содержать от 1 до 200 символов!")
    private String description;
    @NotNull(message = "Ошибка валидации! Дата релиза не может быть пустой!")
    private LocalDate releaseDate;
    @Positive(message = "Ошибка валидации! Продолжительность должна быть больше нуля!")
    private Integer duration;
}
