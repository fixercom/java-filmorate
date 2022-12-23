package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

public interface FriendDao {
    List<Friend> getFriendsForUser(Long userId);
}
