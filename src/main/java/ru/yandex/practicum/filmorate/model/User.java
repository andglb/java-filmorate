package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
    @NotBlank(message = "Ошибка валидации! Email не может быть пустым!")
    @Email(message = "Ошибка валидации! Email должен содержать символ '@'!")
    private String email;
    @NotBlank(message = "Ошибка валидации! Логин не может быть пустым!")
    @Pattern(regexp = "\\S*$", message = "Ошибка валидации! Логин не должен содержать пробелы!")
    private String login;
    private String name;
    @PastOrPresent(message = "Ошибка валидации! День рождения не может быть в будущем!")
    private LocalDate birthday;
    private Set<Long> friends;

    public User(Long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        if (name == null || name.isBlank()) {
            this.name = login;
        }
        this.birthday = birthday;
        this.friends = friends;
        if (friends == null) {
            this.friends =  new HashSet<>();
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}

