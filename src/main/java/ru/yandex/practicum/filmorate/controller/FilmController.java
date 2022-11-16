package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Получен GET запрос /films");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.debug("Получен POST запрос /films \n\trequest body: {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.debug("Получен PUT запрос /films \n\trequest body: {}", film);
        return filmService.updateFilm(film);
    }
}
