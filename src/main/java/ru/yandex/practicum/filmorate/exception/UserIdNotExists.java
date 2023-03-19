package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class UserIdNotExists extends ResponseStatusException {
    public UserIdNotExists(int id) {
        super(HttpStatus.NOT_FOUND, String.format("Пользователь с ИД %d не найден в системе", id));
    }
}
