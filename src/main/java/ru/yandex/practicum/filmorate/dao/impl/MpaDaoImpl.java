package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPA getMpaById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        } catch (DataAccessException e) {
            throw new MpaNotFoundException(id);
        }
    }

    @Override
    public List<MPA> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    public MPA mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}
