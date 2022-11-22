package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ModelTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {
    private FilmStorage filmStorage;

    @BeforeEach
    public void filmServiceInit() {
        filmStorage = new InMemoryFilmStorage();
    }

    @Test
    @DisplayName("Проверка обновления фильма с не существующим id")
    void testUpdateFilm() {
        Film updated = ModelTestUtils.getFilm("updated name", "updated description",
                LocalDate.of(2020, 1, 1), 200);
        updated.setId(9999L);
        Exception exception = assertThrows(NotFoundException.class,
                () -> filmStorage.updateFilm(updated));
        assertEquals("Отсутствует фильм с id=9999", exception.getMessage());
    }
}