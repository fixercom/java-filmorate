package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.DateAfterBirthdayMovie;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotEmpty(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания фильма — 200 символов")
    private String description;
    @DateAfterBirthdayMovie(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private long duration;
    private Set<Long> userIdsWhoLiked = new HashSet<>();

    public void addUserIdWhoLiked(Long userId){
        userIdsWhoLiked.add(userId);
    }

    public boolean removeUserIdWhoLiked(Long userId){
        return userIdsWhoLiked.remove(userId);
    }
}