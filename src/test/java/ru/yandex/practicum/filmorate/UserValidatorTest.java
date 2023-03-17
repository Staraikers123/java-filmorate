package ru.yandex.practicum.filmorate;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

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
    public void shouldThrowExceptionWhenBirthdayIsInFuture() {
        user = builder.email("staraikers@yandex.ru").login("Staraikers").name("Марат")
                .birthday(LocalDate.now().plusWeeks(2)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsNull() {
        user = builder.email(null).login("СофьяПожарская").name("Staraikers")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldSuccessfullyValidateCorrectUser() {
        user = builder.email("staraikers@yandex.ru").login("Staraikers").name("Марат")
                .birthday(LocalDate.of(1997, 12, 19)).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsWithoutAtSymbol() {
        user = builder.email("staraikersyandex.ru").login("Staraikers").name("Марат")
                .birthday(LocalDate.of(1997, Month.DECEMBER, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        user = builder.email("").login("Staraikers").name("Марат")
                .birthday(LocalDate.of(1997, Month.DECEMBER, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldNotThrowExceptionWhenBirthdayIsToday() {
        user = builder.email("staraikers@yandex.ru").login("Staraikers").name("Марат")
                .birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsNull() {
        user = builder.email("staraikers@yandex.ru").login(null).name("Марат")
                .birthday(LocalDate.of(1997, Month.DECEMBER, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginContainsSpace() {
        user = builder.email("staraikersyandex.ru").login("Staraikers 123").name("Марат")
                .birthday(LocalDate.of(1997, Month.DECEMBER, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmpty() {
        user = builder.email("staraikers@yandex.ru").login("").name("Марат")
                .birthday(LocalDate.of(1997, Month.DECEMBER, 19)).build();
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }
}
