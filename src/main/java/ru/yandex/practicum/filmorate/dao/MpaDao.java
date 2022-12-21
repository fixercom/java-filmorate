package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaDao {
    MPA getMpaById(Integer id);

    List<MPA> getAllMpa();
}
