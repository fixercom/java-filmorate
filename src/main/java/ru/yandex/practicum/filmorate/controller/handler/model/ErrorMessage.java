package ru.yandex.practicum.filmorate.controller.handler.model;

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
