package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Сервер получил GET запрос '/users'");
        return userStorage.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable Long id) {
        log.debug("Сервер получил GET запрос '/users/{}'", id);
        return userStorage.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.debug("Сервер получил POST запрос '/users' request body '{}'", user);
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.debug("Сервер получил PUT запрос '/users' request body '{}'", user);
        return userStorage.updateUser(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriendForUser(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("Сервер получил PUT запрос '/users/{}/friends/{}'", id, friendId);
        userService.addFriendForUser(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFriendFromUser(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("Сервер получил DELETE запрос '/users/{}/friends/{}'", id, friendId);
        userService.removeFriendFromUser(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        log.debug("Сервер получил GET запрос '/users/{}/friends'", id);
        return userService.getAllFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("Сервер получил GET запрос '/users/{}/friends/common/{}'", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}