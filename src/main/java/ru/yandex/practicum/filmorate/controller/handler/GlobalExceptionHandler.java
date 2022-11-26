package ru.yandex.practicum.filmorate.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.handler.model.ErrorMessage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(NotFoundException exception) {
        String message = exception.getMessage();
        log.warn("EXCEPTION/NotFound[{}]: {}", 404, message);
        return new ErrorMessage(404, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                              HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}",
                request.getMethod(), request.getRequestURI(), exception.getTarget());
        String fieldName = Objects.requireNonNull(exception.getFieldError()).getField();
        Object rejectedValue = Objects.requireNonNull(exception.getFieldError()).getRejectedValue();
        String message = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        log.warn("EXCEPTION/MethodArgumentNotValid[{}]: поле '{}'='{}' не прошло валидацию по причине '{}'",
                400, fieldName, rejectedValue, message);
        return new ErrorMessage(400, message);
    }
}