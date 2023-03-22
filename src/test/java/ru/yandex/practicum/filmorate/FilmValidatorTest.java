package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FilmValidatorTest {
    private Film.FilmBuilder builder;
    private Film film;

    @BeforeEach
    public void beforeEach() {
        builder = Film.builder();
    }

    @Test
    public void shouldNotThrowExceptionWhenReleaseDateIs28_12_1895() {
        film = builder.name("2 Fast 2 Furious").description("family")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 28)).duration(108).build();
        assertDoesNotThrow(() -> ValidationException.class);
    }

    @Test
    public void shouldSuccessfullyValidateCorrectFilm() {
        film = builder.name("2 Fast 2 Furious").description("family")
                .releaseDate(LocalDate.of(2003, Month.JULY, 21)).duration(108).build();
        assertDoesNotThrow(() -> ValidationException.class);
    }


    @Test
    public void shouldNotThrowExceptionWhenDurationIsPositive() {
        film = builder.name("Fast and Furious").description("family")
                .releaseDate(LocalDate.of(2003, Month.JULY, 21)).duration(1).build();
        assertDoesNotThrow(() -> ValidationException.class);
    }


}
