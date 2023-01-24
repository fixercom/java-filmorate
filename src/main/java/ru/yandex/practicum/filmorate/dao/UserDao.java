package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

public interface UserDao extends UserStorage {
    boolean userHasActiveInvitationFromFriend(Long userId, Long friendId);

    void sendFriendInvitation(Long userId, Long friendId);

    void acceptFriendInvitation(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

    List<Long> getFriendIds(Long userId);

    List<User> getAllFriends(Long userId);

    void deleteUser(long id);
}