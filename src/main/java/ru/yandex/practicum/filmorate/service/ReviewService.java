package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewDao reviewDao;
    private final FeedDao feedDao;

    public Review addReview(Review review) {
        review = reviewDao.addReview(review);
        feedDao.addEventForUser(review.getUserId(), "REVIEW", "ADD", review.getReviewId());
        return review;
    }

    public Review updateReview(Review review) {
        review = reviewDao.updateReview(review);
        feedDao.addEventForUser(review.getUserId(), "REVIEW", "UPDATE", review.getReviewId());
        return review;
    }

    public void deleteReview(Long id) {
        Review review = reviewDao.getReviewById(id);
        feedDao.addEventForUser(review.getUserId(), "REVIEW", "REMOVE", review.getReviewId());
        reviewDao.deleteReview(id);
    }

    public Review getReviewById(Long id) {
        return reviewDao.getReviewById(id);
    }

    public List<Review> getReviewsByFilmId(Long filmId, Long count) {
        return reviewDao.getReviewsByFilmId(filmId, count);
    }

    public void addLikeByUser(Long id, Long userId) {
        reviewDao.addLikeByUser(id, userId);
    }

    public void addDislikeByUser(Long id, Long userId) {
        reviewDao.addDislikeByUser(id, userId);
    }

    public void deleteLikeByUser(Long id, Long userId) {
        reviewDao.deleteLikeByUser(id, userId);
    }

    public void deleteDislikeByUser(Long id, Long userId) {
        reviewDao.deleteDislikeByUser(id, userId);
    }
}
