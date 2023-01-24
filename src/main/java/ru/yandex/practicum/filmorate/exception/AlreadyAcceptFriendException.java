package ru.yandex.practicum.filmorate.exception;

public class AlreadyAcceptFriendException extends RuntimeException {
    public AlreadyAcceptFriendException(Long userId, Long friendId) {
        super(String.format("User with id=%d has already accepted" +
                " a friendship invitation from the user with id=%d", userId, friendId));
    }
}
