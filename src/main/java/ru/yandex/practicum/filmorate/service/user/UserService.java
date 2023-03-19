package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
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

    public void addFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        List<User> mutualFriends = new ArrayList<>();
        for (int id : userStorage.getUser(userId).getFriends()) {
            if (userStorage.getUser(friendId).getFriends().contains(id)) {
                mutualFriends.add(userStorage.getUser(id));
            }
        }
        return mutualFriends;
    }

    public List<User> getUserFriends(int userId) {
        List<User> userFriends = new ArrayList<>();
        return userStorage.findAllUser().stream()
                .filter(user -> user.getFriends().contains(userId))
                .collect(Collectors.toList());

    }
}
