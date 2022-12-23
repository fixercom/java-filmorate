package ru.yandex.practicum.filmorate.validate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ValidatorTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonValidatorTest {

    @Test
    @DisplayName("Проверка валидации полей для Film")
    public void checkFilmModel() {
        Film failName = Film.builder()
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(1900, 3, 25))
                .duration(200)
                .build();
        Film failDescription = Film.builder()
                .name("name")
                .description("really long description description description description description " +
                        "description description description description description description description" +
                        " description description description description description description description " +
                        "description description description")
                .releaseDate(LocalDate.of(1900, 3, 25))
                .duration(200)
                .build();
        Film failReleaseDate = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1890, 3, 25))
                .duration(200)
                .build();
        Film failDuration = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1900, 3, 25))
                .duration(-200)
                .build();

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
        User failLogin1 = User.builder()
                .email("yandex@mail.ru")
                .login("login login")
                .name("name")
                .birthday(LocalDate.of(2020, 8, 20))
                .build();
        User failLogin2 = User.builder()
                .email("yandex@mail.ru")
                .login(null)
                .name("name")
                .birthday(LocalDate.of(2020, 8, 20))
                .build();
        User failEmail = User.builder()
                .email("mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1980, 8, 20))
                .build();
        User failBirthday = User.builder()
                .email("test@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2446, 8, 20))
                .build();

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
