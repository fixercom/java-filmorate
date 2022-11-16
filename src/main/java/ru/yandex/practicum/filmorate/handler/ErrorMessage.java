package ru.yandex.practicum.filmorate.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {
    private Integer statusCode;
    private String message;
}
