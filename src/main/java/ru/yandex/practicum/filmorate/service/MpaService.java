package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    @Qualifier("mpaDaoImpl")
    private final MpaDao mpaDao;

    public MPA getMpaById(Integer id) {
        MPA mpaFromDatabase = mpaDao.getMpaById(id);
        log.debug("Из таблицы mpa получен MPA с id={}: {}", id, mpaFromDatabase);
        return mpaFromDatabase;
    }

    public List<MPA> getAllMpa() {
        List<MPA> allMpa = mpaDao.getAllMpa();
        log.debug("Из таблицы mpa получен список всех MPA: {}", allMpa);
        return allMpa;
    }
}
