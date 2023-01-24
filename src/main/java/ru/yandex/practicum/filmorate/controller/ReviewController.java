package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@RequestBody @Valid Review review, HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), review);
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review, HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public List<Review> getReviewsByFilmId(@RequestParam (value = "filmId", defaultValue = "0")@Positive Long filmId,
                                           @RequestParam (value = "count", defaultValue = "10")@Positive Long count,
                                           HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return reviewService.getReviewsByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeByUser(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        reviewService.addLikeByUser(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeByUser(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        reviewService.addDislikeByUser(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeByUser(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        reviewService.deleteLikeByUser(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeByUser(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        reviewService.deleteDislikeByUser(id, userId);
    }

}
