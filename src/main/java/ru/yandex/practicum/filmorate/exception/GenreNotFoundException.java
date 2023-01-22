package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(Integer id) {
        super(String.format("There is no genre with id=%d in the database", id));
    }
}
