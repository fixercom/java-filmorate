package ru.yandex.practicum.filmorate.exception;

public class LikeDoesNotExist extends RuntimeException {
    public LikeDoesNotExist(Long filmId, Long userId) {
        super(String.format("User with id=%d didn't like film with id=%d", userId, filmId));
    }
}