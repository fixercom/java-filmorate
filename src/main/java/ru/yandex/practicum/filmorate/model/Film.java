package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validate.annotation.DateAfterBirthdayMovie;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotEmpty(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания фильма — 200 символов")
    private String description;
    @DateAfterBirthdayMovie(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    private Integer rate;
    private MPA mpa;
    private Set<Director> directors;
    private Set<Genre> genres;
    private Set<Long> userIdsWhoLiked;
}