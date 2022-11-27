package ru.yandex.practicum.filmorate.validate;

import ru.yandex.practicum.filmorate.validate.annotation.DateAfterBirthdayMovie;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateAfterBirthdayMovieValidator implements ConstraintValidator<DateAfterBirthdayMovie, LocalDate> {

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        if (releaseDate != null) {
            return releaseDate.isAfter(LocalDate.of(1895,12,28));
        }
        return true;
    }
}