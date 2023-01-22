package ru.yandex.practicum.filmorate.exception;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException(Long id) {
        super(String.format("There is no director with id=%d in the database", id));
    }
}
