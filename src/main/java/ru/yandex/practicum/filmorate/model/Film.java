package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class Film {
    private int id;
    private final FilmStorage filmStorage;

    @NotBlank
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Set<Integer> likes;

    public void addLikeToFilm(int id) {
        likes.add(id);
    }

    public void removeLikeFromFilm(int id) {
        likes.remove(id);
    }

    public Integer getRatingFromFilm() {
        int size = likes.size();
        return size;
    }
}