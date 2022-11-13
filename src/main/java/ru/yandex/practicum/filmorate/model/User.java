package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    private long id;
    @Email(message = "Не верный формат e-mail")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(message = "Логин не может содержать пробелы", regexp = "\\S+")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
