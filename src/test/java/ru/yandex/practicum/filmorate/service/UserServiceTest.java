package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void filmServiceInit() {
        userService = new UserService();
    }

    @Test
    @DisplayName("Проверка создания пользователя с не валидными полями")
    void createUser() {
        User failLogin = new User(0, "yandex@mail.ru", "dolore ullamco", "name",
                LocalDate.of(2446, 8, 20));
        User failEmail = new User(0, "mail.ru", "login", "name",
                LocalDate.of(1980, 8, 20));
        User failBirthday = new User(0, "test@mail.ru", "login", "name",
                LocalDate.of(2446, 8, 20));
        User emptyName = new User(0, "friend@common.ru","common", "",
                LocalDate.of(2000, 8, 20));

        Exception firstException = assertThrows(ValidationException.class,
                () -> userService.createUser(failLogin));
        Exception secondException = assertThrows(ValidationException.class,
                () -> userService.createUser(failEmail));
        Exception thirdException = assertThrows(ValidationException.class,
                () -> userService.createUser(failBirthday));
        userService.createUser(emptyName);

        assertAll(
                () -> assertEquals("Логин не может быть пустым и содержать пробелы",
                        firstException.getMessage()),
                () -> assertEquals("Не верный формат e-mail",
                        secondException.getMessage()),
                () -> assertEquals("Дата рождения не может быть в будущем",
                        thirdException.getMessage()),
                () -> assertEquals("common", emptyName.getName())
        );
    }

    @Test
    @DisplayName("Проверка обновления пользователя с не существующим id")
    void updateUser() {
        User original = new User(0, "mail@mail.ru", "login","name",
                LocalDate.of(2000,1,1));
        userService.createUser(original);
        User updated = new User(1, "updated@mail.ru", "updated","updated",
                LocalDate.of(2005, 4, 17));
        updated.setId(9999);
        Exception exception = assertThrows(NotFoundException.class,
                () -> userService.updateUser(updated));
        assertEquals("Отсутствует пользователь с id=9999", exception.getMessage());



    }
}