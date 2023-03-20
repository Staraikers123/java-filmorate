package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        user.addFriendToUser(friendId);
        friend.addFriendToUser(userId);
        log.info("Друг для пользователя '{}' успешно добавлен", user.getName());
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        friend.removeFriendFromUser(userId);
        user.removeFriendFromUser(friendId);
        log.info("Друг для пользователя '{}' успешно удален", user.getName());
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        List<User> mutualFriends = new ArrayList<>();
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        for (int id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                mutualFriends.add(userStorage.getUser(id));
            }
        }
        log.info("Общие друзья для пользователя '{}' успешно обновлены", user.getName());
        return mutualFriends;
    }

    public List<User> getUserFriends(int userId) {
        log.debug("Друзья для пользователя '{}' получены", userStorage.getUser(userId).getName());
        return userStorage.findAllUsers().stream()
                .filter(user -> user.getFriends().contains(userId))
                .collect(Collectors.toList());
    }

    private static void validate(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Имя для отображения может быть пустым - в таком случае будет использован логин");
            user.setName(user.getLogin());
        }
    }
}
