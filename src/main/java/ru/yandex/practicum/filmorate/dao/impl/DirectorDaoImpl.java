package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director createDirector(Director director) {
        Map<String, Object> parametersForDirectorsTable = new HashMap<>();
        parametersForDirectorsTable.put("director_name", director.getName());
        Long directorId = new SimpleJdbcInsert(jdbcTemplate).withTableName("directors")
                .usingGeneratedKeyColumns("director_id")
                .executeAndReturnKey(parametersForDirectorsTable).longValue();
        director.setId(directorId);
        return getDirectorById(directorId);
    }

    @Override
    public Director getDirectorById(Long id) {
        String sql = "SELECT * FROM directors WHERE director_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToDirector, id);
        } catch (DataAccessException e) {
            throw new DirectorNotFoundException(id);
        }
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT * FROM directors";
        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    @Override
    public Director updateDirector(Director director) {
        String sql = "UPDATE directors SET director_name = ? WHERE director_id = ?";
        jdbcTemplate.update(sql, director.getName(), director.getId());
        return getDirectorById(director.getId());
    }

    @Override
    public int deleteDirector(Long id) {
        String sql = "DELETE FROM directors WHERE director_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Set<Director> getDirectorsByFilmId(Long filmId) {
        String sql = "SELECT fd.director_id, d.director_name FROM film_directors AS fd" +
                " JOIN directors AS d ON fd.director_id=d.director_id WHERE fd.film_id=?";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowToDirector, filmId));
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("director_id"))
                .name(resultSet.getString("director_name"))
                .build();
    }
}
