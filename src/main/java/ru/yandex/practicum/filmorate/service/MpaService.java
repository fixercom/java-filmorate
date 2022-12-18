package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaService {
    @Qualifier("mpaDaoImpl")
    private final MpaDao mpaDao;

    public MPA getMpaById(Integer id) {
        return mpaDao.getMpaById(id);
    }

    public List<MPA> getAllMpa() {
        return mpaDao.getAllMpa();
    }
}
