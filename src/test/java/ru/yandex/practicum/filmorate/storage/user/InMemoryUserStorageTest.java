package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryUserStorageTest {
    private UserStorage userStorage;

    @BeforeEach
    public void userServiceInit() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    @DisplayName("Проверка создания пользователя с пустым именем")
    void testCreateUser() {
        User emptyName = User.builder()
                .email("friend@common.ru")
                .login("common")
                .name("")
                .birthday(LocalDate.of(2000, 8, 20))
                .build();
        userStorage.createUser(emptyName);
        assertEquals("common", emptyName.getName());
    }

    @Test
    @DisplayName("Проверка обновления пользователя с не существующим id")
    void testUpdateUser() {
        User updated = User.builder()
                .email("updated@mail.ru")
                .login("updated")
                .name("updated")
                .birthday(LocalDate.of(2005, 4, 17))
                .build();
        updated.setId(9999L);
        Exception exception = assertThrows(NotFoundException.class,
                () -> userStorage.updateUser(updated));
        assertEquals("Отсутствует пользователь с id=9999", exception.getMessage());
    }
}