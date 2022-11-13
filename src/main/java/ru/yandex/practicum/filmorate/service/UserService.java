package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private long currentId;
    private final HashMap<Long, User> users;

    public UserService() {
        currentId = 0;
        users = new HashMap<>();
    }

    public User createUser(User user) {
        setDefaultNameIfEmptyOrNull(user);
        user.setId(++currentId);
        saveUserToMemory(user);
        log.debug("Пользователь {} сохранен в памяти, присвоен id={}", user.getName(), user.getId());
        return user;
    }

    public User updateUser(User user) {
        throwNotFoundExceptionIfIdDoesNotExist(user);
        saveUserToMemory(user);
        log.debug("Пользователь с id={} успешно обновлен", user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void setDefaultNameIfEmptyOrNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void throwNotFoundExceptionIfIdDoesNotExist(User user) {
        if (!(users.containsKey(user.getId()))) {
            throw new NotFoundException("Отсутствует пользователь с id=" + user.getId());
        }
    }

    private void saveUserToMemory(User user) {
        users.put(user.getId(), user);
    }
}