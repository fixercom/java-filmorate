package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    @Qualifier("genreDaoImpl")
    private final GenreDao genreDao;

    public Genre getGenreById(Integer id) {
        return genreDao.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }
}
