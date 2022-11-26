package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private long currentId;
    private final HashMap<Long, User> users;

    public InMemoryUserStorage() {
        currentId = 0;
        users = new HashMap<>();
    }

    @Override
    public User createUser(User user) {
        setDefaultNameIfEmptyOrNull(user);
        user.setId(++currentId);
        saveUserToMemory(user);
        log.debug("Пользователь сохранен в памяти, присвоен id={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long userId = user.getId();
        throwNotFoundExceptionIfIdDoesNotExist(userId);
        saveUserToMemory(user);
        log.debug("Пользователь с id={} обновлен", userId);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Список пользователей получен");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        throwNotFoundExceptionIfIdDoesNotExist(id);
        log.debug("Пользователь с id={} получен", id);
        return users.get(id);
    }

    private void setDefaultNameIfEmptyOrNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void throwNotFoundExceptionIfIdDoesNotExist(Long userId) {
        if (!(users.containsKey(userId))) {
            throw new NotFoundException("Отсутствует пользователь с id=" + userId);
        }
    }

    private void saveUserToMemory(User user) {
        users.put(user.getId(), user);
    }
}
