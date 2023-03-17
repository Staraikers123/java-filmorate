package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;

@Slf4j
public class FilmValidator {
    private static final LocalDate FILM_REALISE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    public static boolean validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().equals("")) {
            log.error("Пустое название фильма");
            throw new ValidationException("Пустое название фильма");
        }
        return false;
    }
}
