package ru.yandex.practicum.filmorate.validate.annotation;


import ru.yandex.practicum.filmorate.validate.DateAfterBirthdayMovieValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateAfterBirthdayMovieValidator.class)
@Documented
public @interface DateAfterBirthdayMovie {
    String message() default "{DateAfterBirthdayMovie.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}