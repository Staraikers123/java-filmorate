package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public List<Film> findAllFilm();

    public Film add(Film film);

    public Film update(Film film);

    public Film getFilm(int id);
}
