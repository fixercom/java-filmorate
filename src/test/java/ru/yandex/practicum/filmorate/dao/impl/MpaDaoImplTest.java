package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.MPA;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDaoImplTest {
    @Qualifier("mpaDaoImpl")
    private final MpaDao mpaDao;

    @Test
    @DisplayName("Проверка получения MPA по id")
    void getMpaByIdTest() {
        MPA mpa = MPA.builder()
                .id(1)
                .name("G")
                .build();
        assertEquals(mpa, mpaDao.getMpaById(1));
    }

    @Test
    @DisplayName("Проверка получения всех MPA")
    void getAllMpaTest() {
        assertEquals(5, mpaDao.getAllMpa().size());
    }
}