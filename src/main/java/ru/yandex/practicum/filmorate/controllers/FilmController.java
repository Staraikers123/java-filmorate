package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.file.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        return filmStorage.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return filmStorage.update(film);
    }

    @GetMapping
    public List<Film> findAllFilm() {
        return filmStorage.findAllFilm();
    }

    @GetMapping("/{id}")
    public Film getFilm(@Valid @PathVariable String id) {
        return filmStorage.getFilm(Integer.parseInt(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public void putALike(@Valid @PathVariable String id, @Valid @PathVariable String userId) {
        filmService.putALike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@Valid @PathVariable String id, @Valid @PathVariable String userId) {
        filmService.deleteLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Valid @RequestParam(defaultValue = "10") String count) {
        return filmService.getRating(Integer.parseInt(count));
    }
}
