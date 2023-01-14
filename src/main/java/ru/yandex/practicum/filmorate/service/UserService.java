package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFriendException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Qualifier("userDaoImpl")
    private final UserDao userDao;

    public User createUser(User user) {
        initNameIfEmptyOrNullValue(user);
        User userFromDatabase = userDao.createUser(user);
        log.debug("Пользователь сохранен в таблице users, присвоен id={}", userFromDatabase.getId());
        return userFromDatabase;
    }

    public User getUserById(Long id) {
        User userFromDatabase = userDao.getUserById(id);
        log.debug("Из таблицы users считан пользователь с id={}: {}", userFromDatabase.getId(), userFromDatabase);
        return userFromDatabase;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = userDao.getAllUsers();
        log.debug("Из таблицы users считаны все пользователи: {}", allUsers);
        return allUsers;
    }

    public User updateUser(User user) {
        User userFromDatabase = userDao.updateUser(user);
        log.debug("Пользователь с id={} обновлен в таблице users", userFromDatabase.getId());
        return userDao.updateUser(user);
    }

    public void addFriendForUser(Long userId, Long friendId) {
        userDao.getUserById(userId);    //throws UserNotFoundException
        userDao.getUserById(friendId);
        if (userDao.userHasActiveInvitationFromFriend(userId, friendId)) {
            userDao.acceptFriendInvitation(userId, friendId);
            log.debug("Пользователь с id={} принял приглашение дружбы от пользователя с id={}, " +
                    "обновлена запись в таблице friends", friendId, userId);
        } else {
            log.debug("Пользователь с id={} отправил приглашение дружбы пользователю с id={}, " +
                    "добавлена запись в таблицу friends", userId, friendId);
            userDao.sendFriendInvitation(userId, friendId);
        }
    }

    public void removeFriendFromUser(Long userId, Long friendId) {
        if (userDao.deleteFriend(userId, friendId)) {
            log.debug("Пользователь с id={} удален из друзей пользователя с id={}, " +
                    "удалена запись в таблице friends", friendId, userId);
        } else {
            throw new NotFriendException("Пользователи не являются друзьями");
        }
    }

    public List<User> getAllFriends(Long userId) {
        List<User> allFriends = userDao.getAllFriends(userId);
        log.debug("Список друзей пользователя с id={} считан из таблицы friends: {}", userId, allFriends);
        return allFriends;
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        List<Long> friendIdsForUser = userDao.getFriendIds(userId);
        List<Long> friendIdsForOther = userDao.getFriendIds(otherId);
        friendIdsForUser.retainAll(friendIdsForOther);
        List<User> commonFriends = friendIdsForUser.stream()
                .map(userDao::getUserById)
                .collect(Collectors.toList());
        log.debug("Из таблицы friends получен список общих друзей для пользователей с id={},{}: {}",
                userId, otherId, commonFriends);
        return commonFriends;
    }

    public void initNameIfEmptyOrNullValue(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void delete(long id) {
        userDao.delete(id);
    }

}