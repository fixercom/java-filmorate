package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ModelTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void userServiceInit() {
        userService = new UserService();
    }

    @Test
    @DisplayName("Проверка создания пользователя с пустым именем")
    void testCreateUser() {
        User emptyName = ModelTestUtils.getUser("friend@common.ru", "common", "",
                LocalDate.of(2000, 8, 20));
        userService.createUser(emptyName);
        assertEquals("common", emptyName.getName());
    }

    @Test
    @DisplayName("Проверка обновления пользователя с не существующим id")
    void testUpdateUser() {
        User updated = ModelTestUtils.getUser("updated@mail.ru", "updated","updated",
                LocalDate.of(2005, 4, 17));
        updated.setId(9999);
        Exception exception = assertThrows(NotFoundException.class,
                () -> userService.updateUser(updated));
        assertEquals("Отсутствует пользователь с id=9999", exception.getMessage());
    }
}