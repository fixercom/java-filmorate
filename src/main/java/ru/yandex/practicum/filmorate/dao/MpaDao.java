package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface MpaDao {
    MPA getMpaById(Integer id);

    List<MPA> getAllMpa();

    MPA mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException;
}
