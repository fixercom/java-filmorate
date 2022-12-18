package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface FilmDao extends FilmStorage {
    void createGenresForFilm(Long filmId, List<Integer> genreIds);

    Set<Genre> getGenresByFilmId(Long film_id);

    void deleteGenresForFilm(Long filmId);

    void saveLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Set<Long> getUserIdsWhoLikedByFilmId(Long filmId);

    Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException;
}
