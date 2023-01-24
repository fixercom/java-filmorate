package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorDao {
    Director createDirector(Director director);

    Director getDirectorById(Long id);

    List<Director> getAllDirectors();

    Director updateDirector(Director director);

    int deleteDirector(Long id);

    Set<Director> getDirectorsByFilmId(Long filmId);
}
