package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface FeedDao {
    void addEventForUser(Long userId, String eventType, String operation, Long entityId);

    List<Event> getFeedForUser(Long userId);

    void deleteFeedForUser(Long userId);
}
