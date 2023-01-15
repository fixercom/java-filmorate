package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewDao {

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(Long id);

    Review getReviewById(Long id);

    List<Review> getReviewsByFilmId(Long filmId, Long count);

    void addLikeByUser(Long id, Long userId);

    void addDislikeByUser(Long id, Long userId);

    void deleteLikeByUser(Long id, Long userId);

    void deleteDislikeByUser(Long id, Long userId);
}
