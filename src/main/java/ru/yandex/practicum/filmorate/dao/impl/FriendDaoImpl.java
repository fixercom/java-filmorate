package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.enums.FriendStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Friend> getFriendsForUser(Long userId) {
        String sql = "SELECT friend_id, status FROM friends WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToFriend, userId);
    }

    public Friend mapRowToFriend(ResultSet resultSet, int rowNum) throws SQLException {
        return Friend.builder()
                .id(resultSet.getLong("friend_id"))
                .status(Enum.valueOf(FriendStatus.class, resultSet.getString("status")))
                .build();
    }
}