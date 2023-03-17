package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    public static void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Имя для отображения может быть пустым - в таком случае будет использован логин");
            user.setName(user.getLogin());
        }
        if (user.getLogin() == null || user.getLogin().equals("")) {
            log.info("Поле логин не может быть пустым");
            throw new ValidationException("Поле логин не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().equals("")) {
            log.info("Поле Email не может быть пустым");
            throw new ValidationException("Поле Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.info("Email не содержит символ @");
            throw new ValidationException("Email не содержит символ @");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Возраст указан некорректно");
            throw new ValidationException("Возраст указан некорректно");
        }
    }
}
