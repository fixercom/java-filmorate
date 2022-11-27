package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {
    @Override
    public User createUser(User user) {
        setDefaultNameIfEmptyOrNull(user);
        user.setId(++currentId);
        saveToStorage(user);
        log.debug("Пользователь сохранен в памяти, присвоен id={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        throwNotFoundExceptionIfIdDoesNotExist(id, "Отсутствует пользователь с id=");
        updateInStorage(id, user);
        log.debug("Пользователь с id={} обновлен", id);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Список пользователей получен");
        return getAllElementsFromStorage();
    }

    @Override
    public User getUserById(Long id) {
        throwNotFoundExceptionIfIdDoesNotExist(id, "Отсутствует пользователь с id=");
        log.debug("Пользователь с id={} получен", id);
        return loadFromStorage(id);
    }

    private void setDefaultNameIfEmptyOrNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
