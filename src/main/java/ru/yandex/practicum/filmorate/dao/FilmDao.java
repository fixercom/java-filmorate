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

    List<Film> getTopFilms(Integer count);

    List<Film> getFilmsByDirectorWithoutSorting(Long directorId);

    List<Film> getFilmsByDirectorSortByLikes(Long directorId);

    List<Film> getFilmsByDirectorSortByYear(Long directorId);

    void deleteDirectorsForFilm(Long filmId);

    void delete(long id);

    List<Film> getCommonFilms(Long userId, Long friendId);

    List<Film> getSearchByTitle(String query);

    List<Film> getSearchByDirector(String query);

    List<Film> getSearchByDirectorAndTitle(String query);

    List<Film> getFilmsRecommendFilmsForUsers(Long id);

    List<Film> getTopFilmsByGenreAndYear(Integer count, Integer genreId, Integer year);

    List<Film> getTopFilmsByGenre(Integer count, Integer genreId);

    List<Film> getTopFilmsByYear(Integer count, Integer year);
}
