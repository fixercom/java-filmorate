package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyLikedThisFilm extends RuntimeException {
    public UserAlreadyLikedThisFilm(Long userId, Long filmId) {
        super(String.format("User with id=%d has already put a like to the film with id=%d", userId, filmId));
    }
}