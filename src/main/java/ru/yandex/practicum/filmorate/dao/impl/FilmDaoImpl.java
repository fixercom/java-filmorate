package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final DirectorDao directorDao;

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> parametersForFilmsTable = new HashMap<>();
        parametersForFilmsTable.put("film_name", film.getName());
        parametersForFilmsTable.put("description", film.getDescription());
        parametersForFilmsTable.put("release_date", film.getReleaseDate());
        parametersForFilmsTable.put("duration", film.getDuration());
        parametersForFilmsTable.put("rate", 0);
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
        String sql = "UPDATE films SET film_name = ?, description = ?, release_date = ?," +
                " duration = ?, rate = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public void createGenresForFilm(Long filmId, List<Integer> genreIds) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?,?)";
        genreIds.forEach(genreId -> jdbcTemplate.update(sql, filmId, genreId));
    }

    @Override
    public void deleteGenresForFilm(Long filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void saveLike(Long filmId, Long userId) {
        String sql = "MERGE INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Set<Long> getUserIdsWhoLikedByFilmId(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Long.class, filmId));
    }

    @Override
    public void createDirectorsForFilm(Long filmId, List<Long> directorIds) {
        String sql = "INSERT INTO film_directors (film_id, director_id) VALUES (?,?)";
        directorIds.forEach(directorId -> jdbcTemplate.update(sql, filmId, directorId));
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sql = "SELECT *" +
                " FROM films" +
                " ORDER BY rate DESC" +
                " LIMIT VALUES (?)";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    @Override
    public List<Film> getFilmsByDirectorWithoutSorting(Long directorId) {
        String sql = "SELECT f.*" +
                " FROM films AS f JOIN film_directors AS fd ON f.film_id = fd.film_id" +
                " WHERE  fd.director_id= ?";
        return jdbcTemplate.query(sql, this::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> getFilmsByDirectorSortByLikes(Long directorId) {
        String sql = "SELECT films.*" +
                " FROM films" +
                " WHERE film_id IN (" +
                "    SELECT film_id" +
                "    FROM film_directors" +
                "    WHERE director_id = ?)" +
                "ORDER BY rate DESC";
        return jdbcTemplate.query(sql, this::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> getFilmsByDirectorSortByYear(Long directorId) {
        String sql = "SELECT films.*" +
                " FROM films" +
                " WHERE film_id IN (" +
                "    SELECT film_id" +
                "    FROM film_directors" +
                "    WHERE director_id = ?)" +
                "ORDER BY release_date";
        return jdbcTemplate.query(sql, this::mapRowToFilm, directorId);
    }

    @Override
    public void deleteDirectorsForFilm(Long filmId) {
        String sql = "DELETE FROM film_directors WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(mpaDao.getMpaById(resultSet.getInt("mpa_id")))
                .directors(directorDao.getDirectorsByFilmId(resultSet.getLong("film_id")))
                .genres(genreDao.getGenresByFilmId(resultSet.getLong("film_id")))
                .userIdsWhoLiked(getUserIdsWhoLikedByFilmId(resultSet.getLong("film_id")))
                .build();
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sql = "SELECT F.* FROM FILMS AS F" +
                " LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID" +
                " WHERE F.FILM_ID IN (SELECT DISTINCT F2.FILM_ID FROM (SELECT LIKES.FILM_ID FROM LIKES WHERE USER_ID = ?) AS F1" +
                " INNER JOIN (SELECT LIKES.FILM_ID FROM LIKES WHERE USER_ID = ?) AS F2 ON F1.FILM_ID = F2.FILM_ID)" +
                " GROUP BY F.FILM_ID" +
                " ORDER BY COUNT(L.FILM_ID) DESC";
        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId);
    }

    @Override
    public List<Film> getSearchByTitle(String query) {
        String fullQuery = "'%" + query + "%'";
        String preSql = "SELECT * FROM FILMS AS F" +
                " LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID" +
                " WHERE LOWER(FILM_NAME) LIKE LOWER(%s)" +
                " GROUP BY L.FILM_ID" +
                " ORDER BY COUNT(L.FILM_ID) DESC";
        String sql = String.format(preSql, fullQuery);
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> getSearchByDirector(String query) {
        String fullQuery = "'%" + query + "%'";
        String preSql = "SELECT F.* FROM FILMS AS F" +
                " JOIN FILM_DIRECTORS AS FD ON F.FILM_ID = FD.FILM_ID" +
                " JOIN DIRECTORS AS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID" +
                " LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID" +
                " WHERE LOWER(D.DIRECTOR_NAME) LIKE LOWER(%s)" +
                " GROUP BY L.FILM_ID" +
                " ORDER BY COUNT(L.FILM_ID) DESC";
        String sql = String.format(preSql, fullQuery);
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> getSearchByAll(String query) {
        String fullQuery = "'%" + query + "%'";
        String preSql = "SELECT F.* FROM FILMS AS F" +
                " LEFT JOIN FILM_DIRECTORS AS FD ON F.FILM_ID = FD.FILM_ID" +
                " LEFT JOIN DIRECTORS AS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID" +
                " LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID" +
                " WHERE LOWER(F.FILM_NAME) LIKE LOWER(%s)" +
                " OR LOWER(D.DIRECTOR_NAME) LIKE LOWER(%s)" +
                " GROUP BY L.FILM_ID" +
                " ORDER BY COUNT(L.FILM_ID) DESC";
        String sql = String.format(preSql, fullQuery, fullQuery);
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsRecommendFilmsForUsers(Long userId) {
        String sqlGetRecommendedFilmsIds = "SELECT DISTINCT film_id\n" +
                " FROM likes " +
                " WHERE film_id NOT IN " +
                "    (SELECT film_id " +
                "     FROM likes " +
                "     WHERE user_id = ?) " +
                "  AND user_id IN " +
                "    (SELECT user_id AS other_user_id " +
                "     FROM likes " +
                "     WHERE film_id IN " +
                "         (SELECT film_id " +
                "          FROM likes " +
                "          WHERE user_id = ?) " +
                "     GROUP BY other_user_id " +
                "     ORDER BY COUNT (film_id) DESC " +
                "     LIMIT 10) ";
        List<Long> filmsId = jdbcTemplate.queryForList(sqlGetRecommendedFilmsIds, Long.class, userId
                , userId);
        return filmsId.stream().map(this::getFilmById).collect(Collectors.toList());
    }

    @Override
    public List<Film> getTopFilmsByGenreAndYear(Integer count, Integer genreId, Integer year) {
        String sql = "SELECT *" +
                " FROM films" +
                " WHERE film_id IN(" +
                "    SELECT film_id" +
                "    FROM film_genres" +
                "    WHERE genre_id = ?)" +
                "    AND EXTRACT(YEAR FROM CAST(release_date AS DATE)) = ?" +
                " ORDER BY rate" +
                " LIMIT VALUES (?)";
        return jdbcTemplate.query(sql, this::mapRowToFilm, genreId, year, count);
    }

    @Override
    public List<Film> getTopFilmsByGenre(Integer count, Integer genreId) {
        String sql = "SELECT *" +
                " FROM films" +
                " WHERE film_id IN(" +
                "    SELECT film_id" +
                "    FROM film_genres" +
                "    WHERE genre_id = ?)" +
                " ORDER BY rate" +
                " LIMIT VALUES (?)";
        return jdbcTemplate.query(sql, this::mapRowToFilm, genreId, count);
    }

    @Override
    public List<Film> getTopFilmsByYear(Integer count, Integer year) {
        String sql = "SELECT *" +
                " FROM films" +
                " WHERE EXTRACT(YEAR FROM CAST(release_date AS DATE)) = ?" +
                " ORDER BY rate" +
                " LIMIT VALUES (?)";
        return jdbcTemplate.query(sql, this::mapRowToFilm, year, count);
    }

}