package ru.yandex.practicum.filmorate.exception;

public class AlreadyInviteFriendException extends RuntimeException {
    public AlreadyInviteFriendException(Long userId, Long friendId) {
        super(String.format("User with id=%d has already sent a friendship invitation" +
                " to the user with id=%d", userId, friendId));
    }
}
