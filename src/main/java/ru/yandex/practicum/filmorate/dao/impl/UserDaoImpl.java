package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.AlreadyAcceptFriendException;
import ru.yandex.practicum.filmorate.exception.AlreadyInviteFriendException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final FriendDao friendDao;

    @Override
    public User createUser(User user) {
        Map<String, Object> parametersForUsersTable = new HashMap<>();
        parametersForUsersTable.put("email", user.getEmail());
        parametersForUsersTable.put("login", user.getLogin());
        parametersForUsersTable.put("user_name", user.getName());
        parametersForUsersTable.put("birthday", user.getBirthday());
        Long userId = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("user_id")
                .executeAndReturnKey(parametersForUsersTable).longValue();
        return getUserById(userId);
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (DataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday =?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return getUserById(user.getId());
    }

    public boolean userHasActiveInvitationFromFriend(Long userId, Long friendId) {
        String sql = "SELECT status FROM friends WHERE user_id = ? AND friend_id = ?";
        try {
            String status = jdbcTemplate.queryForObject(sql, String.class, friendId, userId);
            assert status != null;
            if (status.equals("CONFIRMED")) {
                throw new AlreadyAcceptFriendException(userId, friendId);
            }
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public void sendFriendInvitation(Long userId, Long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, userId, friendId, "UNCONFIRMED");
        } catch (DuplicateKeyException e) {
            throw new AlreadyInviteFriendException(userId, friendId);
        }
    }

    public void acceptFriendInvitation(Long userId, Long friendId) {
        String sql = "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, "CONFIRMED", friendId, userId);
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sql, userId, friendId) > 0;
    }

    public List<User> getAllFriends(Long userId) {
        getUserById(userId);
        String sql = "SELECT u.user_id, u.email, u.login, u.user_name, u.birthday" +
                " FROM friends AS f JOIN users AS u ON f.friend_id = u.user_id WHERE  f.user_id  = ?";
        return jdbcTemplate.query(sql, this::mapRowToUser, userId);
    }

    public List<Long> getFriendIds(Long userId) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("user_name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(friendDao.getFriendsForUser(resultSet.getLong("user_id")))
                .build();
    }

    @Override
    public void deleteUser(long id) {
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
    }

}
