package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
@Slf4j
public class MpaStorage {
    JdbcTemplate jdbcTemplate;

    public MpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpa(int mpaId) {
        String sql = "SELECT MPA_NAME FROM RATES_MPA WHERE MPA_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, mpaId);
        if (userRows.next()) {
            log.info("Получение рейтинга");
            return new Mpa(mpaId,
                    userRows.getString("mpa_name"));
        } else {
            log.info("Получение не найден");
            return null;
        }
    }
}
