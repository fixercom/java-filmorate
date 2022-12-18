package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface FriendDao {
    List<Friend> getFriendsForUser(Long userId);

    Friend mapRowToFriend(ResultSet resultSet, int rowNum) throws SQLException;
}
