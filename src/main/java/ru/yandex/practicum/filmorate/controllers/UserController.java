package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User add(@Valid @RequestBody User user) throws ValidationException {
        return userStorage.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping
    public List<User> findAllUser() {
        return userStorage.findAllUser();
    }

    @GetMapping("/{id}")
    public User getUser(@Valid @PathVariable String id) {
        return userStorage.getUser(Integer.parseInt(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@Valid @PathVariable String id, @Valid @PathVariable String friendId) {
        userStorage.getUser(Integer.parseInt(id));
        userStorage.getUser(Integer.parseInt(friendId));
        userService.addFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@Valid @PathVariable String id, @Valid @PathVariable String friendId) {
        userStorage.getUser(Integer.parseInt(id));
        userStorage.getUser(Integer.parseInt(friendId));
        userService.deleteFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@Valid @PathVariable String id) {
        return userService.getUserFriends(Integer.parseInt(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@Valid @PathVariable String id, @Valid @PathVariable String otherId) {
        return userService.getMutualFriends(Integer.parseInt(id), Integer.parseInt(otherId));
    }
}
