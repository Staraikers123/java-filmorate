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
        if (film.getDescription().length() > 200) {
            log.info("Описание не может быть больше 200 символов");
            throw new ValidationException("Описание не может быть больше 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.info("Длительность не может быть меньше 0");
            throw new ValidationException("Длительность не может быть меньше 0");
        }
        if (film.getReleaseDate().isBefore(FILM_REALISE_DATE)) {
            log.info("Дата фильма должна быть не позже 28.12.1895");
            throw new ValidationException("Дата фильма должна быть не позже 28.12.1895");
        }
        return false;
    }
}
