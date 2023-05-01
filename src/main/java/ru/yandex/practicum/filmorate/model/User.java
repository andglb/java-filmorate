package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Integer id;
    @NotBlank(message = "Ошибка валидации! Email не может быть пустым!")
    @Email(message = "Ошибка валидации! Email должен содержать символ '@'!")
    private String email;
    @NotBlank(message = "Ошибка валидации! Логин не может быть пустым!")
    @Pattern(regexp = "\\S*$", message = "Ошибка валидации! Логин не должен содержать пробелы!")
    private String login;
    private String name;
    @PastOrPresent(message = "Ошибка валидации! День рождения не может быть в будущем!")
    private LocalDate birthday;
}
