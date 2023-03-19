package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserIdNotExists;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

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

    public List<User> findAllUserl() {
        return new ArrayList<>(saveUserStorage.values());
    }

    @Override
    public User add(User user) {
        if (saveUserStorage.values().stream().noneMatch(saveUser -> saveUser.getLogin().equals(user.getLogin()))) {
            UserValidator.validate(user);
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
    public List<User> findAllUser() {
        log.debug("Текущее количество пользователей: '{}'", saveUserStorage.size());
        return new ArrayList<>(saveUserStorage.values());
    }

    @Override
    public User getUser(int id) {
        if (!saveUserStorage.containsKey(id)) {
            throw new UserIdNotExists(id);
        }
        return saveUserStorage.get(id);
    }
}
