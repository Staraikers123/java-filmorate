package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {
    private User.UserBuilder builder;
    private User user;

    @BeforeEach
    public void beforeEach() {
        builder = User.builder();
    }


    @Test
    public void shouldSuccessfullyValidateCorrectUser() {
        user = builder.email("staraikers@yandex.ru").login("Staraikers").name("Марат")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    public void shouldNotThrowExceptionWhenBirthdayIsToday() {
        user = builder.email("staraikers@yandex.ru").login("Staraikers").name("Марат")
                .birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

}
