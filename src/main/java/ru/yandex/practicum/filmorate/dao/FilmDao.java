package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;

public interface FilmDao extends FilmStorage {
    void createGenresForFilm(Long filmId, List<Integer> genreIds);

    void deleteGenresForFilm(Long filmId);

    void saveLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Set<Long> getUserIdsWhoLikedByFilmId(Long filmId);

    void createDirectorsForFilm(Long filmId, List<Long> directorIds);

    List<Film> getFilmsByDirector(Long directorId);

    void deleteDirectorsForFilm(Long filmId);

    void delete(long id);
}
