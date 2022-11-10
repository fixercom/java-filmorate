package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class UserService {
    private long currentId;
    private final HashMap<Long, User> users;

    public UserService() {
        currentId = 0;
        users = new HashMap<>();
    }

    public User createUser(User user) {
        checkEmail(user);
        checkLogin(user);
        checkName(user);
        checkBirthday(user);
        user.setId(++currentId);
        saveUserToMemory(user);
        log.debug("Пользователь {} сохранен в памяти, присвоен id={}", user.getName(), user.getId());
        return user;
    }

    public User updateUser(User user) {
        checkId(user);
        saveUserToMemory(user);
        log.debug("Пользователь с id={} успешно обновлен", user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void checkEmail(User user) {
        if ((user.getEmail().isBlank()) || !(user.getEmail().contains("@"))) {
            throw new ValidationException("Не верный формат e-mail");
        }
    }

    private void checkLogin(User user) {
        if ((user.getLogin().isBlank()) || (user.getLogin().split(" ").length > 1)) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void checkBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private void checkId(User user) {
        if (!(users.containsKey(user.getId()))) {
            throw new NotFoundException("Отсутствует пользователь с id=" + user.getId());
        }
    }

    private void saveUserToMemory(User user) {
        users.put(user.getId(), user);
    }
}