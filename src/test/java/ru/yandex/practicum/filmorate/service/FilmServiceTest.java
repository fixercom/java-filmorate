package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    private FilmService filmService;

    @BeforeEach
    public void filmServiceInit() {
        filmService = new FilmService();
    }

    @Test
    @DisplayName("Проверка создания фильма с не валидными полями")
    void createFilm() {
        Film failName = new Film(0, "", "Description",
                LocalDate.of(1900, 3, 25), 200);
        Film failDescription = new Film(0, "Film name",
                "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                        " а именно 20 миллионов. о Куглов, который за время «своего отсутствия»," +
                        " стал кандидатом Коломбани.",
                LocalDate.of(1900, 3, 25),
                200);
        Film failReleaseDate = new Film(0, "Name ", "Description",
                LocalDate.of(1890, 3, 25), 200);
        Film failDuration = new Film(0, "Name", "Description",
                LocalDate.of(1900, 3, 25), -200);
        Exception firstException = assertThrows(ValidationException.class,
                () -> filmService.createFilm(failName));
        Exception secondException = assertThrows(ValidationException.class,
                () -> filmService.createFilm(failDescription));
        Exception thirdException = assertThrows(ValidationException.class,
                () -> filmService.createFilm(failReleaseDate));
        Exception fourthException = assertThrows(ValidationException.class,
                () -> filmService.createFilm(failDuration));
        assertAll(
                () -> assertEquals("Название фильма не может быть пустым",
                        firstException.getMessage()),
                () -> assertEquals("Максимальная длина описания фильма — 200 символов",
                        secondException.getMessage()),
                () -> assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года",
                        thirdException.getMessage()),
                () -> assertEquals("Продолжительность фильма должна быть положительной",
                        fourthException.getMessage())
        );
    }

    @Test
    @DisplayName("Проверка обновления фильма с не существующим id")
    void updateFilm() {
        Film original = new Film(0, "Name", "Description",
                LocalDate.of(1900, 3, 25), 200);
        filmService.createFilm(original);
        Film updated = new Film(1, "Film Updated", "New film update description",
                LocalDate.of(1989, 4, 17), 190);
        updated.setId(9999);
        Exception exception = assertThrows(NotFoundException.class,
                () -> filmService.updateFilm(updated));
        assertEquals("Отсутствует фильм с id=9999", exception.getMessage());
    }
}