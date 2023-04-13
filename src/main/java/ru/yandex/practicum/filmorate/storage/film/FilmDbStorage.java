package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        if (film.getGenres() != null) {
            String sqlToFilmGenre = "INSERT INTO FILM_GENRES (genre_id, film_id) VALUES(?, ?)";
            List<Genre> genres = List.copyOf(film.getGenres());
            jdbcTemplate.batchUpdate(sqlToFilmGenre, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, genres.get(i).getId());
                    ps.setInt(2, film.getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        }
        jdbcTemplate.update("UPDATE FILMS SET RATE_ID = ? WHERE FILM_ID = ?",
                film.getMpa().getId(),
                film.getId()
        );

        log.info("Новый фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (isFilmExists(film.getId())) {
            String sqlQuery = "UPDATE FILMS SET " +
                    "name = ?, description = ?, releaseDate = ?, duration = ?, " +
                    "rate_id = ? WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            genreStorage.updateGenresOfFilm(film);
            log.info("Фильм {} обновлен", film);
            return film;
        } else {
            throw new NotFoundException(String.format("Фильм с %d не найден", film.getId()));
        }
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sql = "SELECT * " +
                "FROM FILMS " +
                "LEFT JOIN RATES_MPA as R ON FILMS.RATE_ID = R.MPA_ID ";
        log.info("Получение списка фильмов");
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                FindfilmGenres(rs.getInt("film_id")),
                new Mpa(rs.getInt("rate_id"), rs.getString("mpa_name"))
        ));
    }

    @Override
    public Optional<Film> getFilm(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS " +
                "LEFT JOIN RATES_MPA as R ON FILMS.RATE_ID = R.MPA_ID " +
                "WHERE film_id = ?", id);

        if (userRows.next()) {
            Film film = new Film(
                    userRows.getInt("film_id"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate").toLocalDate(),
                    userRows.getInt("duration"),
                    new Mpa(userRows.getInt("rate_id"), userRows.getString("mpa_name"))
            );
            Set<Genre> genres = genreStorage.getFilmGenres(id);
            if (genres.size() != 0) {
                film.setGenres(genreStorage.getFilmGenres(id));
            }
            log.info("Фильм с id {} найден", id);
            return Optional.of(film);
        } else {
            log.error("Фильм с id {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public Film delete(Film film) {
        if (isFilmExists(film.getId())) {
            String sql = "DELETE FROM FILMS WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getId());
            log.info("Фильм {} удален", film);
            return film;
        } else {
            log.error("Фильм не найден");
            throw new NotFoundException("Фильм не найден");
        }
    }

    public boolean isFilmExists(int id) {
        log.info("Проверка фильма");
        String sql = "SELECT * FROM FILMS WHERE film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        return userRows.next();
    }

    private Set<Genre> FindfilmGenres(int filmId) {
        Set<Genre> genres = new TreeSet<>();
        String sql = "SELECT * FROM GENRES AS g JOIN FILM_GENRES AS fg ON g.genre_id = fg.genre_id WHERE fg.film_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, filmId);
        while (rows.next()) {
            genres.add(new Genre(rows.getInt("genre_id"), rows.getString("genre")));
        }
        log.info("Данные по жанрам получены");
        return genres;
    }
}
