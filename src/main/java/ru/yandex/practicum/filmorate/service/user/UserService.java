package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validate(user);
        log.info("Пользователь '{}' успешно прошел валидацию", user.getName());
        return userStorage.add(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + userId + " не существует");
        }
        if (user == friend) {
            throw new NotFoundException("Пользователь с id=" + friendId + " не существует");
        }
        userStorage.makeFriends(userId, friendId);
        log.info("Друг для пользователя '{}' успешно добавлен", user.getName());
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        userStorage.removeFriends(userId, friendId);
        userStorage.removeFriends(friendId, userId);
        log.info("Друг для пользователя '{}' успешно удален", user.getName());
    }

    public List<User> getMutualFriends(int userId) {
        log.info("Друзья обновлены");
        List<User> friends = new ArrayList<>();
        List<Integer> friendsIds = userStorage.getUserFriendsById(userId);
        if (friendsIds == null) {
            return friends;
        }
        for (int friendIds : friendsIds) {
            User friend = userStorage.getUser(friendIds);
            friends.add(friend);
        }
        return friends;
    }

    private static void validate(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Имя для отображения может быть пустым - в таком случае будет использован логин");
            user.setName(user.getLogin());
        }
    }
}
