package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("mpaDaoImpl")
    private final MpaDao mpaDao;
    @Qualifier("genreDaoImpl")
    private final GenreDao genreDao;

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> parametersForFilmsTable = new HashMap<>();
        parametersForFilmsTable.put("film_name", film.getName());
        parametersForFilmsTable.put("description", film.getDescription());
        parametersForFilmsTable.put("release_date", film.getReleaseDate());
        parametersForFilmsTable.put("duration", film.getDuration());
        parametersForFilmsTable.put("mpa_id", film.getMpa().getId());
        Long filmId = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("film_id")
                .executeAndReturnKey(parametersForFilmsTable).longValue();
        film.setId(filmId);
        return getFilmById(filmId);
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (DataAccessException e) {
            String errorMessage = String.format("В базе данных отсутствует фильм с id=%d", id);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film updateFilm(Film film) {
        filmCheck(film.getId());
        String sql = "UPDATE films SET film_name = ?, description = ?, release_date = ?," +
                " duration = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public void createGenresForFilm(Long filmId, List<Integer> genreIds) {
        filmCheck(filmId);
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?,?)";
        genreIds.forEach(genreId -> jdbcTemplate.update(sql, filmId, genreId));
    }

    @Override
    public void deleteGenresForFilm(Long filmId) {
        filmCheck(filmId);
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void saveLike(Long filmId, Long userId) {
        filmCheck(filmId);
        userCheck(userId);
        String sql = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        filmCheck(filmId);
        userCheck(userId);
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Set<Long> getUserIdsWhoLikedByFilmId(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Long.class, filmId));
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDao.getMpaById(resultSet.getInt("mpa_id")))
                .genres(genreDao.getGenresByFilmId(resultSet.getLong("film_id")))
                .userIdsWhoLiked(getUserIdsWhoLikedByFilmId(resultSet.getLong("film_id")))
                .build();
    }

    @Override
    public void delete(long id) {
        filmCheck(id);
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    private void filmCheck(long filmId) {
        String checkFilm = "select * from FILMS where FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkFilm, filmId);
        if (!rs.next()) {
            throw new NotFoundException("Film not found");
        }
    }

    private void userCheck(long userId) {
        String checkUser = "select * from USERS where USER_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkUser, userId);
        if (!rs.next()) {
            throw new NotFoundException("User not found");
        }
    }
}