package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        assertDoesNotThrow(() -> UserService.validate(user));
    }


    @Test
    public void shouldNotThrowExceptionWhenBirthdayIsToday() {
        user = builder.email("staraikers@yandex.ru").login("Staraikers").name("Марат")
                .birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserService.validate(user));
    }

}
