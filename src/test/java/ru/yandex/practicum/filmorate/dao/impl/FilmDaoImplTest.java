package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoImplTest {

    private final FilmDao filmDao;

    @Test
    @DisplayName("Проверка сохранения фильма в БД")
    void createFilmTest() {
        Film originalFilm = createFilmForTest();
        Film filmFromDatabase = filmDao.createFilm(originalFilm);
        originalFilm.setId(filmFromDatabase.getId());
        assertEquals(originalFilm, filmFromDatabase);
    }

    @Test
    @DisplayName("Проверка получения фильма по id")
    void getFilmByIdTest() {
        Film originalFilm = createFilmForTest();
        Long filmId = filmDao.createFilm(originalFilm).getId();
        Film filmFromDatabase = filmDao.getFilmById(filmId);
        originalFilm.setId(filmId);
        assertEquals(originalFilm, filmFromDatabase);
    }

    @Test
    @DisplayName("Проверка получения списка всех фильмов")
    void getAllFilmsTest() {
        int oldNumberOfFilmsInDatabase = filmDao.getAllFilms().size();
        Film newFilm = createFilmForTest();
        filmDao.createFilm(newFilm);
        Integer newNumberOfFilmsInDatabase = filmDao.getAllFilms().size();
        assertEquals(oldNumberOfFilmsInDatabase + 1, newNumberOfFilmsInDatabase);
    }

    @Test
    @DisplayName("Проверка обновления фильма")
    void updateFilmTest() {
        Film film = createFilmForTest();
        Long filmId = filmDao.createFilm(film).getId();
        film.setId(filmId);
        film.setName("Updated");
        filmDao.updateFilm(film);
        assertEquals("Updated", filmDao.getFilmById(filmId).getName());
    }

    @Test
    @DisplayName("Проверка добавления лайка фильму")
    void saveLikeTest() {
        Film film = createFilmForTest();
        Long filmId = filmDao.createFilm(film).getId();
        filmDao.saveLike(filmId, 5L);
        Film filmFromDatabase = filmDao.getFilmById(filmId);
        assertTrue(filmFromDatabase.getUserIdsWhoLiked().contains(5L));
    }

    @Test
    @DisplayName("Проверка удаления лайка у фильма")
    void deleteLikeTest() {
        Film film = createFilmForTest();
        Long filmId = filmDao.createFilm(film).getId();
        filmDao.saveLike(filmId, 33L);
        filmDao.deleteLike(filmId, 33L);
        Film filmFromDatabase = filmDao.getFilmById(filmId);
        assertFalse(filmFromDatabase.getUserIdsWhoLiked().contains(33L));
    }

    @Test
    @DisplayName("Проверка получения списка пользователей поставивших лайк")
    void getUserIdsWhoLikedByFilmIdTest() {
        Film film = createFilmForTest();
        Long filmId = filmDao.createFilm(film).getId();
        filmDao.saveLike(filmId, 55L);
        filmDao.saveLike(filmId, 13L);
        Film filmFromDatabase = filmDao.getFilmById(filmId);
        assertEquals(Set.of(13L, 55L), filmFromDatabase.getUserIdsWhoLiked());
    }

    private Film createFilmForTest() {
        return Film.builder()
                .name("Avatar: The Way Of Water")
                .description("From lush jungles to the gorgeous reefs…")
                .releaseDate(LocalDate.of(2022, 12, 16))
                .duration(192)
                .mpa(MPA.builder()
                        .id(1)
                        .name("G")
                        .build())
                .genres(new HashSet<>())
                .directors(new HashSet<>())
                .userIdsWhoLiked(new HashSet<>())
                .build();
    }
}