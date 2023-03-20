package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
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
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.addFriendToUser(friendId);
        friend.addFriendToUser(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        friend.removeFriendFromUser(userId);
        user.removeFriendFromUser(friendId);
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
        return mutualFriends;
    }

    public List<User> getUserFriends(int userId) {
        return userStorage.findAllUsers().stream()
                .filter(user -> user.getFriends().contains(userId))
                .collect(Collectors.toList());
    }

    public static void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Имя для отображения может быть пустым - в таком случае будет использован логин");
            user.setName(user.getLogin());
        }
    }
}
