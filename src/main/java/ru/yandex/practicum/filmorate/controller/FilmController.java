package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Сервер получил GET запрос '/films'");
        return filmStorage.getAllFilms();
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.debug("Сервер получил GET запрос '/films/{}'", id);
        return filmStorage.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.debug("Сервер получил POST запрос '/films' request body '{}'", film);
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.debug("Сервер получил PUT запрос '/films' request body '{}'", film);
        return filmStorage.updateFilm(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Сервер получил PUT запрос '/films/{}/like/{}'", id, userId);
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Сервер получил DELETE запрос '/films/{}/like/{}'", id, userId);
        filmService.removeLikeFromFilm(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopFilms(@RequestParam Optional<Integer> count) {
        log.debug("Сервер получил GET запрос '/popular'");
        return filmService.getTopFilms(count.orElse(10));
    }
}
