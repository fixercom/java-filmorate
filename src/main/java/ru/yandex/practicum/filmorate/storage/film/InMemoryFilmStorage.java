package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {

    @Override
    public Film createFilm(Film film) {
        film.setId(++currentId);
        saveToStorage(film);
        log.debug("Фильм сохранен в памяти, присвоен id={}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Long id = film.getId();
        throwNotFoundExceptionIfIdDoesNotExist(id, "Отсутствует фильм с id=");
        updateInStorage(id, film);
        log.debug("Фильм с id={} обновлен", id);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Список фильмов получен");
        return getAllElementsFromStorage();
    }

    @Override
    public Film getFilmById(Long id) {
        throwNotFoundExceptionIfIdDoesNotExist(id, "Отсутствует фильм с id=");
        log.debug("Фильм с id={} получен", id);
        return loadFromStorage(id);
    }
}