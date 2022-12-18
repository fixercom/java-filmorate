package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        return userDao.createUser(user);
    }

    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    public void addFriendForUser(Long userId, Long friendId) {
        userDao.getUserById(userId);    //throws UserNotFoundException
        userDao.getUserById(friendId);
        if (userDao.userHasActiveInvitationFromFriend(userId, friendId)) {
            userDao.acceptFriendInvitation(userId, friendId);
        } else {
            userDao.sendFriendInvitation(userId, friendId);
        }
        log.debug("Пользователь с id={} добавлен в друзья пользователя с id={}", friendId, userId);
    }

    public void removeFriendFromUser(Long userId, Long friendId) {
        if (userDao.deleteFriend(userId, friendId)) {
            log.debug("Пользователь с id={} удален из друзей пользователя с id={}", friendId, userId);
        } else {
            throw new NotFoundException("Не существует пользователя с id=" + friendId);
        }
    }

    public List<User> getAllFriends(Long userId) {
        log.debug("Список друзей пользователя с id={} получен", userId);
        return userDao.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        List<Long> friendIdsForUser = userDao.getFriendIds(userId);
        List<Long> friendIdsForOther = userDao.getFriendIds(otherId);
        friendIdsForUser.retainAll(friendIdsForOther);
        log.debug("Список общий друзей для пользователей с id={},{} получен", userId, otherId);
        return friendIdsForUser.stream().map(userDao::getUserById).collect(Collectors.toList());
    }

    public void initNameIfEmptyOrNullValue(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}