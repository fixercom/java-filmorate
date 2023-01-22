package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedDaoImpl implements FeedDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addEventForUser(Long userId, String eventType, String operation, Long entityId) {
        jdbcTemplate.update("insert into events (USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) " +
                "values (?, ?, ?, ?)", userId, eventType, operation, entityId);
    }

    @Override
    public List<Event> getFeedForUser(Long userId) {
        String sql = "SELECT * FROM events WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, this::mapRowToFeed, userId);
    }

    @Override
    public void deleteFeedForUser(Long userId) {
        String sql = "DELETE FROM events WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    private Event mapRowToFeed(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .timestamp(resultSet.getTimestamp("TIMESTAMP").getTime())
                .userId(resultSet.getLong("USER_ID"))
                .eventType(resultSet.getString("EVENT_TYPE"))
                .operation(resultSet.getString("OPERATION"))
                .eventId(resultSet.getLong("EVENT_ID"))
                .entityId(resultSet.getLong("ENTITY_ID"))
                .build();
    }
}