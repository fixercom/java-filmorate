package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.ModelTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserStorage userStorage;
    private UserService userService;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    @Test
    @DisplayName("Проверка добавления в друзья пользователя")
    void testAddFriendForUser() {
        User user = ModelTestUtils.getUser("mail@mail.ru", "login", "name", LocalDate.now());
        User newFriendForUser = ModelTestUtils.getUser("m@m.ru", "login2", "name", LocalDate.now());
        userStorage.createUser(user);
        userStorage.createUser(newFriendForUser);
        userService.addFriendForUser(1L, 2L);
        assertAll(
                () -> assertTrue(userStorage.getUserById(1L).getFriendIds().contains(2L),
                        "Пользователь id=1 должен содержать друга с id=2"),
                () -> assertTrue(userStorage.getUserById(2L).getFriendIds().contains(1L),
                        "Пользователь id=2 должен содержать друга с id=1")
        );
    }

    @Test
    @DisplayName("Проверка удаления друга у пользователя")
    void testRemoveFriendFromUser() {
        User user = ModelTestUtils.getUser("mail@mail.ru", "login", "name", LocalDate.now());
        User newFriendForUser = ModelTestUtils.getUser("m@m.ru", "login2", "name", LocalDate.now());
        userStorage.createUser(user);
        userStorage.createUser(newFriendForUser);
        userService.addFriendForUser(1L, 2L);
        assertEquals(1, userStorage.getUserById(1L).getFriendIds().size(),
                "Пользователь id=1 должен иметь содержать одного друга");
        userService.removeFriendFromUser(1L, 2L);
        assertEquals(0, userStorage.getUserById(1L).getFriendIds().size(),
                "У пользователя с id=1 должно быть 0 друзей");
    }

    @Test
    @DisplayName("Проверка получения списка друзей")
    void testGetAllFriends() {
        User user = ModelTestUtils.getUser("mail@mail.ru", "login", "name", LocalDate.now());
        User newFriend1ForUser = ModelTestUtils.getUser("m@m.ru", "login2", "name2", LocalDate.now());
        User newFriend2ForUser = ModelTestUtils.getUser("m@k.ru", "login3", "name3", LocalDate.now());
        userStorage.createUser(user);
        userStorage.createUser(newFriend1ForUser);
        userStorage.createUser(newFriend2ForUser);
        userService.addFriendForUser(1L, 2L);
        userService.addFriendForUser(1L, 3L);
        assertAll(
                () -> assertTrue(userService.getAllFriends(1L).contains(newFriend1ForUser),
                        "Список всех друзей должен содержать пользователя newFriend1ForUser"),
                () -> assertTrue(userService.getAllFriends(1L).contains(newFriend2ForUser),
                        "Список всех друзей должен содержать пользователя newFriend2ForUser")
        );
    }

    @Test
    void testGetCommonFriends() {
        User user1 = ModelTestUtils.getUser("m@m.ru", "login1", "name1", LocalDate.now());
        User user2 = ModelTestUtils.getUser("l@m.ru", "login2", "name2", LocalDate.now());
        User commonUser = ModelTestUtils.getUser("g@k.ru", "login3", "name3", LocalDate.now());
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(commonUser);
        userService.addFriendForUser(1L, 3L);
        assertEquals(0, userService.getCommonFriends(1L, 2L).size(),
                "У пользователей с id=1 и id=2 должно быть 0 общих друзей");
        userService.addFriendForUser(2L, 3L);
        assertEquals(1, userService.getCommonFriends(1L, 2L).size(),
                "У пользователей с id=1 и id=2 должен быть 1 общий друг");
    }
}