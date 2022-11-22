package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private long currentId;
    private final HashMap<Long, Film> films;

    public InMemoryFilmStorage() {
        currentId = 0;
        films = new HashMap<>();
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++currentId);
        saveFilmToMemory(film);
        log.debug("OK[{}]: Фильм сохранен в памяти, присвоен id={}", 200, film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Long filmId = film.getId();
        throwNotFoundExceptionIfIdDoesNotExist(filmId);
        saveFilmToMemory(film);
        log.debug("OK[{}]: Фильм с id={} обновлен", 200, filmId);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("OK[{}]: Список фильмов получен", 200);
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        throwNotFoundExceptionIfIdDoesNotExist(id);
        log.debug("OK[{}]: Фильм с id={} получен", 200, id);
        return films.get(id);
    }

    private void throwNotFoundExceptionIfIdDoesNotExist(Long filmId) {
        if (!(films.containsKey(filmId))) {
            throw new NotFoundException("Отсутствует фильм с id=" + filmId);
        }
    }

    private void saveFilmToMemory(Film film) {
        films.put(film.getId(), film);
    }
}
