package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyLikedThisFilm extends RuntimeException{
    public UserAlreadyLikedThisFilm(String message) {
        super(message);
    }
}