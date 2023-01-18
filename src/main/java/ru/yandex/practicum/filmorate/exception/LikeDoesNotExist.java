package ru.yandex.practicum.filmorate.exception;

public class LikeDoesNotExist extends RuntimeException {
    public LikeDoesNotExist(String message) {
        super(message);
    }
}