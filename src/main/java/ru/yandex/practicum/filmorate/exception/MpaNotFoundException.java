package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(Integer id) {
        super(String.format("There is no MPA with id=%d in the database", id));
    }
}
