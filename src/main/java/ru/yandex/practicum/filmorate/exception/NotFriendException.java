package ru.yandex.practicum.filmorate.exception;

public class NotFriendException extends RuntimeException {
    public NotFriendException(Long userId, Long friendId) {
        super(String.format("User with id=%d is not a friend for the user with id=%d", friendId, userId));
    }
}