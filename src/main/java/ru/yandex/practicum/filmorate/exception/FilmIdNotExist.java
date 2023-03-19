package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class FilmIdNotExist extends ResponseStatusException {
    public FilmIdNotExist(int id) {
        super(HttpStatus.NOT_FOUND, String.format("Фильм с ИД %d не найден", id));
    }
}
