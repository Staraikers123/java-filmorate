package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int id = 1;
    private final HashMap<Integer, Film> saveFilmStorage = new HashMap<>();

    @Override
    public Film add(Film film) {
        FilmService.validate(film);
        if (saveFilmStorage.values().stream().noneMatch((saveFilm -> saveFilm.getName().equals(film.getName())))) {
            film.setId(id++);
            film.setLikes(new HashSet<>());
            saveFilmStorage.put(film.getId(), film);
            log.info("Фильм '{}' успешно добавлен", film.getName());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (saveFilmStorage.containsKey(film.getId())) {
            film.setLikes(new HashSet<>());
            saveFilmStorage.put(film.getId(), film);
            log.info("Фильм '{}' успешно обновлен", film.getName());
            return film;
        } else {
            log.error("Фильм '{}' не найден", film.getName());
            throw new ValidationException("Ошибка обновления данных о фильма");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        log.debug("Текущее количество фильмов: '{}'", saveFilmStorage.size());
        return new ArrayList<>(saveFilmStorage.values());
    }

    @Override
    public Film getFilm(int id) {
        if (!saveFilmStorage.containsKey(id)) {
            log.error(String.format("Фильм с ИД %d не найден", id));
            throw new NotFoundException(String.format("Фильм с ИД %d не найден", id));
        }
        log.info(String.format("Фильм с ИД %d найден", id));
        return saveFilmStorage.get(id);
    }
}
