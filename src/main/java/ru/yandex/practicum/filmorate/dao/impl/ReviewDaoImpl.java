package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final FilmDao filmDao;

    @Qualifier("feedDaoImpl")
    private final FeedDao feedDao;

    @Override
    public Review addReview(Review review) {
        userDao.getUserById(review.getUserId());
        filmDao.getFilmById(review.getFilmId());
        String sql = "INSERT INTO REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            stmt.setInt(5, 0);
            return stmt;
        }, keyHolder);
        feedDao.addFeed(review.getUserId(), "REVIEW", "ADD", Objects.requireNonNull(keyHolder.getKey()).longValue());
        return getReviewById(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE REVIEWS SET CONTENT = ?, IS_POSITIVE = ? WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        feedDao.addFeed(review.getUserId(), "REVIEW", "UPDATE", review.getFilmId());
        return getReviewById(review.getReviewId());
    }

    @Override
    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, id);
        feedDao.addFeed(review.getUserId(), "REVIEW", "REMOVE", review.getReviewId());
    }

    @Override
    public Review getReviewById(Long id) {
        String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToReview, id);
        } catch (DataAccessException e) {
            String errorMessage = String.format("В базе данных отсутствует отзыв с id=%d", id);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public List<Review> getReviewsByFilmId(Long filmId, Long count) {
        List<Review> reviews;
        if (filmId == 0) {
            String sql = "SELECT * FROM REVIEWS ORDER BY USEFUL DESC LIMIT ?";
            reviews = jdbcTemplate.query(sql, this::mapRowToReview, count);
        } else {
            String sql = "SELECT * FROM REVIEWS WHERE FILM_ID = ? ORDER BY USEFUL DESC LIMIT ?";
            reviews = jdbcTemplate.query(sql, this::mapRowToReview, filmId, count);
        }
        return reviews;
    }

    @Override
    public void addLikeByUser(Long id, Long userId) {
        String sql = "MERGE INTO REVIEW_LIKES (REVIEW_ID, USER_ID, IS_USEFUL) VALUES (?, ?, TRUE)";
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
       // feedDao.addFeed(userId, "LIKE", "ADD", id);
    }

    @Override
    public void addDislikeByUser(Long id, Long userId) {
        String sql = "MERGE INTO REVIEW_LIKES (REVIEW_ID, USER_ID, IS_USEFUL) VALUES (?, ?, FALSE)";
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
       // feedDao.addFeed(userId, "LIKE", "ADD", id);
    }

    @Override
    public void deleteLikeByUser(Long id, Long userId) {
        String sql = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_USEFUL = TRUE";
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
       // feedDao.addFeed(userId, "LIKE", "REMOVE", id);
    }

    @Override
    public void deleteDislikeByUser(Long id, Long userId) {
        String sql = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_USEFUL = FALSE";
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
        //feedDao.addFeed(userId, "LIKE", "REMOVE", id);
    }

    private void updateUseful(Long id) {
        String sql = "UPDATE REVIEWS SET USEFUL = " +
                "(SELECT COUNT(IS_USEFUL) FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND IS_USEFUL = TRUE) - " +
                "(SELECT COUNT(IS_USEFUL) FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND IS_USEFUL = FALSE) " +
                "WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, id, id, id);
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getLong("REVIEW_ID"))
                .content(resultSet.getString("CONTENT"))
                .isPositive(resultSet.getBoolean("IS_POSITIVE"))
                .userId(resultSet.getLong("USER_ID"))
                .filmId(resultSet.getLong("FILM_ID"))
                .useful(resultSet.getInt("USEFUL"))
                .build();
    }
}
