package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedDao {
    void addFeed(Long userId, String eventType, String operation, Long entityId);

    List<Feed> getFeed(Long userId);

    void deleteFeed(Long userId);
}
