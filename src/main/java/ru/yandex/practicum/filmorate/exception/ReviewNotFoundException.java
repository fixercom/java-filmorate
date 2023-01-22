package ru.yandex.practicum.filmorate.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super(String.format("There is no review with id=%d in the database", id));
    }
}
