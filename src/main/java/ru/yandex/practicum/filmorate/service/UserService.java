package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public void addFriendForUser(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriendId(friendId);
        friend.addFriendId(userId);
        log.debug("OK[{}]: Пользователь с id={} добавлен в друзья пользователя с id={}", 200, friendId, userId);
    }

    public void removeFriendFromUser(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.removeFriendId(friendId);
        friend.removeFriendId(userId);
        log.debug("OK[{}]: Пользователь с id={} удален из друзей пользователя с id={}", 200, friendId, userId);
    }

    public List<User> getAllFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        log.debug("OK[{}]: Список друзей пользователя с id={} получен", 200, userId);
        return user.getFriendIds().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.getUserById(userId);
        User other = userStorage.getUserById(otherId);
        log.debug("OK[{}]: Список общий друзей для пользователей с id={},{} получен", 200, userId, otherId);
        return user.getFriendIds().stream()
                .filter(other.getFriendIds()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}