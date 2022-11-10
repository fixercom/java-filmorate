package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class FilmService {
    private long currentId;
    private final HashMap<Long, Film> films;

    public FilmService() {
        currentId = 0;
        films = new HashMap<>();
    }

    public Film createFilm(Film film) {
        checkName(film);
        checkDescription(film);
        checkReleaseDate(film);
        checkDuration(film);
        film.setId(++currentId);
        saveFilmToMemory(film);
        log.debug("Фильм {} сохранен в памяти, присвоен id={}", film.getName(), film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        checkId(film);
        saveFilmToMemory(film);
        log.debug("Фильм с id={} успешно обновлен", film.getId());
        return film;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void checkName(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
    }

    private void checkDescription(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания фильма — 200 символов");
        }
    }

    private void checkReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
        }
    }

    private void checkDuration(Film film) {
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    private void checkId(Film film) {
        if (!(films.containsKey(film.getId()))) {
            throw new NotFoundException("Отсутствует фильм с id=" + film.getId());
        }
    }

    private void saveFilmToMemory(Film film) {
        films.put(film.getId(), film);
    }
}