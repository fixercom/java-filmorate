package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.FriendStatus;

@Data
@Builder
public class Friend {
    private Long id;
    private FriendStatus status;
}
