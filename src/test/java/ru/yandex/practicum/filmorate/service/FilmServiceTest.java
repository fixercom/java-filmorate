package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.utils.ModelTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private FilmStorage filmStorage;
    private FilmService filmService;

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    @Test
    @DisplayName("Проверка добавления лайка фильму")
    void testAddLikeToFilm() {
        Film film = ModelTestUtils.getFilm("film", "descr.", LocalDate.now(), 90);
        filmStorage.createFilm(film);
        filmService.addLikeToFilm(1L, 1024L);
        assertAll(
                () -> assertEquals(1, filmStorage.getFilmById(1L).getUserIdsWhoLiked().size(),
                        "Фильм с id=1 должен содержать один лайк"),
                () -> assertTrue(filmStorage.getFilmById(1L).getUserIdsWhoLiked().contains(1024L),
                        "Фильм с id=1 должен содержать id пользователя = 1024")
        );
    }

    @Test
    @DisplayName("Проверка удаления лайка с фильма")
    void testRemoveLikeFromFilm() {
        Film film = ModelTestUtils.getFilm("film", "descr.", LocalDate.now(), 90);
        filmStorage.createFilm(film);
        //Добавляем два лайка фильму
        filmService.addLikeToFilm(1L, 33L);
        //Удалаем лайк с id пользователя = 33L
        filmService.addLikeToFilm(1L, 11L);
        filmService.removeLikeFromFilm(1L, 33L);
        assertAll(
                () -> assertEquals(1, filmStorage.getFilmById(1L).getUserIdsWhoLiked().size(),
                        "Фильм с id=1 должен содержать один лайк"),
                () -> assertFalse(filmStorage.getFilmById(1L).getUserIdsWhoLiked().contains(33L),
                        "Фильм с id=1 не должен содержать id пользователя = 33")
        );
    }

    @Test
    @DisplayName("Проверка топ фильмов по кол-ву лайков")
    void testGetTopFilms() {
        Film film1 = ModelTestUtils.getFilm("film1", "descr.1", LocalDate.now(), 90);
        Film film2 = ModelTestUtils.getFilm("film2", "descr.2", LocalDate.now(), 90);
        Film film3 = ModelTestUtils.getFilm("film3", "descr.3", LocalDate.now(), 90);
        Film film4 = ModelTestUtils.getFilm("film4", "descr.4", LocalDate.now(), 90);
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        filmStorage.createFilm(film3);
        filmStorage.createFilm(film4);
        //Два лайка первому фильму
        filmService.addLikeToFilm(1L, 1L);
        filmService.addLikeToFilm(1L, 2L);
        //Три лайка третьему фильму
        filmService.addLikeToFilm(3L, 3L);
        filmService.addLikeToFilm(3L, 4L);
        filmService.addLikeToFilm(3L, 5L);
        //Один лайк четвертому фильму
        filmService.addLikeToFilm(4L, 6L);
        //Получаем топ фильмов с ограничением в первых два фильма
        List<Film> topFilms = filmService.getTopFilms(2);
        assertAll(
                () -> assertEquals(2, topFilms.size(), "В топе фильмов должно быть два фильма"),
                () -> assertEquals(3L, topFilms.get(0).getId(), "Топ 1 должен быть фильм с id=3"),
                () -> assertEquals(1L, topFilms.get(1).getId(), "Топ 2 должен быть фильм с id=1")
        );
    }
}