package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public void addLikeToFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.addUserIdWhoLiked(userId);
        log.debug("Лайк пользователя с id={} добавлен для фильма с id={}", userId, filmId);
    }

    public void removeLikeFromFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (!(film.removeUserIdWhoLiked(userId))) {
            throw new NotFoundException("Пользователь с id=" + userId + " не существует");
        }
        log.debug("Лайк пользователя с id={} удален для фильма с id={}", userId, filmId);
    }

    public List<Film> getTopFilms(Integer count) {
        log.debug("Топ фильмов с ограничением в {} шт. получен", count);
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getUserIdsWhoLiked().size() - film1.getUserIdsWhoLiked().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}