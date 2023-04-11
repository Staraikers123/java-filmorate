package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
@Slf4j
public class GenreService {
    private final JdbcTemplate jdbcTemplate;

    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Genre> getGenres() {
        log.info("Получение жанров");
        return jdbcTemplate.query("SELECT * FROM GENRES",
                ((rs, rowNum) -> new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre"))
                ));
    }

    public Genre getGenreById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT GENRE FROM GENRES WHERE GENRE_ID = ?", id);
        if (userRows.next()) {
            Genre genre = new Genre(
                    id,
                    userRows.getString("genre")
            );
            log.info("Получение жанра {}", genre);
            return genre;
        } else {
            throw new NotFoundException(String.format("Нет жанра с id %d", id));
        }
    }
}