package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 0;
    private final HashMap<Integer, User> saveUserStorage = new HashMap<>();

    @PostMapping
    public User add(@Valid @RequestBody User user) throws ValidationException {
        UserValidator.validate(user);
        if (saveUserStorage.values().stream().noneMatch(saveUser -> saveUser.getLogin().equals(user.getLogin()))) {
            user.setId(id++);
            saveUserStorage.put(user.getId(), user);
            log.info("Пользователь '{}' успешно добавлен", user.getLogin());
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (saveUserStorage.containsKey(user.getId())) {
            saveUserStorage.put(user.getId(), user);
            log.info("Данные пользователя '{}' успешно обновлены", user.getLogin());
            return user;
        } else {
            log.error("Данные пользователя '{}' небыли изменены", user.getName());
            throw new ValidationException("Ошибка обновления данных пользователя");
        }
    }

    @GetMapping
    public List<User> findAllUser() {
        log.debug("Текущее количество пользователей: '{}'", saveUserStorage.size());
        return new ArrayList<>(saveUserStorage.values());
    }
}
