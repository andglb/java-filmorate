package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым!")
    @NotNull(message = "Имя не может быть нулевым!")
    private String name;
    @Size(min =1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
