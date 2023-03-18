package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 1;
    private final HashMap<Integer, Film> saveFilmStorage = new HashMap<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        FilmValidator.validate(film);
        if (saveFilmStorage.values().stream().noneMatch((saveFilm -> saveFilm.getName().equals(film.getName())))) {
            film.setId(id++);
            saveFilmStorage.put(film.getId(), film);
            log.info("Фильм '{}' успешно добавлен", film.getName());
        }
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (saveFilmStorage.containsKey(film.getId())) {
            saveFilmStorage.put(film.getId(), film);
            log.info("Фильм '{}' успешно обновлен", film.getName());
            return film;
        } else {
            log.error("Фильм '{}' не найден", film.getName());
            throw new ValidationException("Ошибка обновления данных о фильма");
        }
    }

    @GetMapping
    public List<Film> get() {
        log.debug("Текущее количество фильмов: '{}'", saveFilmStorage.size());
        return new ArrayList<>(saveFilmStorage.values());
    }
}
