package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> findAllUser();

    public User add(User user);

    public User update(User user);

    public User getUser(int id);
}
