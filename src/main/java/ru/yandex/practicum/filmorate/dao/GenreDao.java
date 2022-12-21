package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre getGenreById(Integer id);

    List<Genre> getAllGenres();

    Set<Genre> getGenresByFilmId(Long film_id);
}
