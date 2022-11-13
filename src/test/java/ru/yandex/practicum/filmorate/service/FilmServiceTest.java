package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ModelTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmServiceTest {
    private FilmService filmService;

    @BeforeEach
    public void filmServiceInit() {
        filmService = new FilmService();
    }
    @Test
    @DisplayName("Проверка обновления фильма с не существующим id")
    void updateFilm() {
        Film updated = ModelTestUtils.getFilm("updated name", "updated description",
                LocalDate.of(2020,1,1),200);
        updated.setId(9999);
        Exception exception = assertThrows(NotFoundException.class,
                () -> filmService.updateFilm(updated));
        assertEquals("Отсутствует фильм с id=9999", exception.getMessage());
    }
}