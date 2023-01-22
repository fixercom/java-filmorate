package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(Long id) {
        super(String.format("There is no film with id=%d in the database", id));
    }
}
