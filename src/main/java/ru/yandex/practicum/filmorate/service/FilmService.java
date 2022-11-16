package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private long currentId;
    private final HashMap<Long, Film> films;

    public FilmService() {
        currentId = 0;
        films = new HashMap<>();
    }

    public Film createFilm(Film film) {
        film.setId(++currentId);
        saveFilmToMemory(film);
        log.debug("Фильм {} сохранен в памяти, присвоен id={}", film.getName(), film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        throwNotFoundExceptionIfIdDoesNotExist(film);
        saveFilmToMemory(film);
        log.debug("Фильм с id={} успешно обновлен", film.getId());
        return film;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void throwNotFoundExceptionIfIdDoesNotExist(Film film) {
        if (!(films.containsKey(film.getId()))) {
            throw new NotFoundException("Отсутствует фильм с id=" + film.getId());
        }
    }

    private void saveFilmToMemory(Film film) {
        films.put(film.getId(), film);
    }
}