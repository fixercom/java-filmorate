package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ModelTestUtils;
import ru.yandex.practicum.filmorate.utils.ValidatorTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonValidatorTest {

    @Test
    @DisplayName("Проверка валидации полей для Film")
    public void checkFilmModel() {
        Film failName = ModelTestUtils.getFilm("", "description",
                LocalDate.of(1900, 3, 25), 200);
        Film failDescription = ModelTestUtils.getFilm("name", "really long description" +
                        " description description description description description description description" +
                        " description description description description description description description" +
                        " description description description description description description description",
                LocalDate.of(1900, 3, 25), 200);
        Film failReleaseDate = ModelTestUtils.getFilm("name ", "description",
                LocalDate.of(1890, 3, 25), 200);
        Film failDuration = ModelTestUtils.getFilm("name", "description",
                LocalDate.of(1900, 3, 25), -200);
        assertAll(
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failName,
                        "Название фильма не может быть пустым"), "1"),
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failDescription,
                        "Максимальная длина описания фильма — 200 символов"), "2"),
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failReleaseDate,
                        "Дата релиза не может быть раньше 28 декабря 1895 года"), "3"),
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failDuration,
                        "Продолжительность фильма должна быть положительной"), "4")
        );
    }
    @Test
    @DisplayName("Проверка валидации полей для User")
    public void checkUserModel() {
        User failLogin1 = ModelTestUtils.getUser("yandex@mail.ru", "login login", "name",
                LocalDate.of(2020, 8, 20));
        User failLogin2 = ModelTestUtils.getUser("yandex@mail.ru", null, "name",
                LocalDate.of(2020, 8, 20));
        User failEmail = ModelTestUtils.getUser("mail.ru", "login", "name",
                LocalDate.of(1980, 8, 20));
        User failBirthday = ModelTestUtils.getUser( "test@mail.ru", "login", "name",
                LocalDate.of(2446, 8, 20));
        assertAll(
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failLogin1,
                        "Логин не может содержать пробелы"),
                        "Валидатор содержит сообщение об ошибке:\n" +
                                "\"Логин не может содержать пробелы\""),
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failLogin2,
                                "Логин не может быть пустым"),
                        "Валидатор содержит сообщение об ошибке:\n" +
                                "\"Логин не может быть пустым\""),
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failEmail,
                        "Не верный формат e-mail"),
                        "Валидатор содержит сообщение об ошибке:\n" +
                                "\"Не верный формат e-mail\""),
                () -> assertTrue(ValidatorTestUtils.modelHasErrorMessage(failBirthday,
                        "Дата рождения не может быть в будущем"),
                        "Валидатор содержит сообщение об ошибке:\n" +
                                "\"Дата рождения не может быть в будущем\"")
        );
    }
}
