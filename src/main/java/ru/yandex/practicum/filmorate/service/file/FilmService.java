package ru.yandex.practicum.filmorate.service.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void putALike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikes().add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (filmStorage.getFilm(filmId).getLikes().contains(userId)) {
            filmStorage.getFilm(filmId).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Указанный пользователь не ставил оценку данному фильму");
        }

    }

    public List<Film> getRating(int count) {
        return filmStorage.findAllFilm().stream().sorted((film1, film2) ->
                        film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}
