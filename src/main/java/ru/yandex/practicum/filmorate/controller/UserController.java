package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Получен GET запрос /users");
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.debug("Получен POST запрос /users \n\trequest body: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateFilm(@RequestBody @Valid User user) {
        log.debug("Получен PUT запрос /users \n\trequest body: {}", user);
        return userService.updateUser(user);
    }
}