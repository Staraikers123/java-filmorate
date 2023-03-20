package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final HashMap<Integer, User> saveUserStorage = new HashMap<>();

    @Override
    public User add(User user) {
        if (saveUserStorage.values().stream().noneMatch(saveUser -> saveUser.getLogin().equals(user.getLogin()))) {
            UserService.validate(user);
            user.setId(id++);
            user.setFriends(new HashSet<>());
            saveUserStorage.put(user.getId(), user);
            log.info("Пользователь '{}' успешно добавлен", user.getLogin());
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (saveUserStorage.containsKey(user.getId())) {
            user.setFriends(new HashSet<>());
            saveUserStorage.put(user.getId(), user);
            log.info("Данные пользователя '{}' успешно обновлены", user.getLogin());
            return user;
        } else {
            log.error("Данные пользователя '{}' небыли изменены", user.getName());
            throw new ValidationException("Ошибка обновления данных пользователя");
        }
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Текущее количество пользователей: '{}'", saveUserStorage.size());
        return new ArrayList<>(saveUserStorage.values());
    }

    @Override
    public User getUser(int id) {
        if (!saveUserStorage.containsKey(id)) {
            log.error(String.format("Пользователь с ИД %d не найден", id));
            throw new NotFoundException(String.format("Пользователь с ИД %d не найден", id));
        }
        log.info(String.format("Пользователь с ИД %d найден", id));
        return saveUserStorage.get(id);
    }
}
