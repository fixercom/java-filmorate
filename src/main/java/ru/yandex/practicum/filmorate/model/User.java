package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    @Email(message = "Не верный формат e-mail")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(message = "Логин не может содержать пробелы", regexp = "\\S+")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private List<Friend> friends;
}
