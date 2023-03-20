package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate FILM_REALISE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        validate(film);
        log.info("Фильм '{}' успешно прошел валидацию", film.getName());
        return filmStorage.add(film);
    }

    public void putALike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        film.addLikeToFilm(userId);
        log.info("Лайк для фильма '{}' успешно добавлен", film.getName());
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film.getLikes().contains(userId)) {
            film.removeLikeFromFilm(userId);
            log.info("Лайк для фильма '{}' успешно удален", film.getName());
        } else {
            log.error("Указанный пользователь не ставил оценку данному фильму'{}'", film.getName());
            throw new NotFoundException("Указанный пользователь не ставил оценку данному фильму");
        }
    }

    public List<Film> getRating(int count) {
        log.info("Рейтинг для фильмов успешно пересчитан");
        return filmStorage.findAllFilms().stream()
                .sorted((film1, film2) ->
                        film2.getRatingFromFilm() - film1.getRatingFromFilm())
                .limit(count)
                .collect(Collectors.toList());
    }

    private static void validate(Film film) {
        if (film.getReleaseDate().isBefore(FILM_REALISE_DATE)) {
            log.info("Дата фильма должна быть не позже 28.12.1895");
            throw new ValidationException("Дата фильма должна быть не позже 28.12.1895");
        }
    }
}
