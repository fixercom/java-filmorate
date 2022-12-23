package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    @Qualifier("genreDaoImpl")
    private final GenreDao genreDao;

    public Genre getGenreById(Integer id) {
        Genre genreFromDatabase = genreDao.getGenreById(id);
        log.debug("Из таблицы genres получен жанр с id={}: {}", id, genreFromDatabase);
        return genreFromDatabase;
    }

    public List<Genre> getAllGenres() {
        List<Genre> allGenres = genreDao.getAllGenres();
        log.debug("Из таблицы genres получен список всех жанров: {}", allGenres);
        return allGenres;
    }
}
