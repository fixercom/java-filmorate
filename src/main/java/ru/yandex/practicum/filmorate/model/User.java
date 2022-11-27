package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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
    private Set<Long> friendIds = new HashSet<>();

    public void addFriendId(Long friendId){
        friendIds.add(friendId);
    }

    public void removeFriendId(Long friendId){
        friendIds.remove(friendId);
    }
}
