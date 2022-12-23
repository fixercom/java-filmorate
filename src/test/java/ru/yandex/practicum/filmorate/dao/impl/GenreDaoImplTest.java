package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImplTest {
    @Qualifier("genreDaoImpl")
    private final GenreDao genreDao;

    @Test
    @DisplayName("Проверка получения жанра по id")
    void getGenreByIdTest() {
        Genre genre = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();
        assertEquals(genre, genreDao.getGenreById(1));
    }

    @Test
    @DisplayName("Проверка получения всех жанров")
    void getAllGenresTest() {
        assertEquals(6, genreDao.getAllGenres().size());
    }
}