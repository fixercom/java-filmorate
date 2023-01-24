package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Director {
    Long id;
    @NotBlank(message = "Имя режиссера не может быть пустым")
    String name;
}
