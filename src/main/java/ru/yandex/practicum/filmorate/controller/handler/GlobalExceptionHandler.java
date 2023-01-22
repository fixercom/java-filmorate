package ru.yandex.practicum.filmorate.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.handler.model.ErrorMessage;
import ru.yandex.practicum.filmorate.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String YELLOW_COLOR_LOG = "\033[33m";
    private static final String ORIGINAL_COLOR_LOG = "\033[0m";

    @ExceptionHandler({NotFoundException.class,
            DirectorNotFoundException.class,
            FilmNotFoundException.class,
            GenreNotFoundException.class,
            MpaNotFoundException.class,
            ReviewNotFoundException.class,
            UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(RuntimeException exception) {
        String message = exception.getMessage();
        log.warn("{}{}: {} {}", YELLOW_COLOR_LOG,
                exception.getClass().getSimpleName(), ORIGINAL_COLOR_LOG, message);
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
        log.warn("{}MethodArgumentNotValidException: {}поле '{}'='{}' не прошло валидацию по причине '{}'",
                YELLOW_COLOR_LOG, ORIGINAL_COLOR_LOG, fieldName, rejectedValue, message);
        return new ErrorMessage(400, message);
    }

    @ExceptionHandler(AlreadyInviteFriendException.class)
    public ErrorMessage handleAlreadyFriendException(AlreadyInviteFriendException exception) {
        String message = exception.getMessage();
        log.warn("{}AlreadyFriendException:{} {}", YELLOW_COLOR_LOG, ORIGINAL_COLOR_LOG, message);
        return new ErrorMessage(200, message);
    }

    @ExceptionHandler(AlreadyAcceptFriendException.class)
    public ErrorMessage handleAlreadyAcceptFriendException(AlreadyAcceptFriendException exception) {
        String message = exception.getMessage();
        log.warn("{}AlreadyAcceptFriendException:{} {}", YELLOW_COLOR_LOG, ORIGINAL_COLOR_LOG, message);
        return new ErrorMessage(200, message);
    }

    @ExceptionHandler(NotFriendException.class)
    public ErrorMessage handleNotFriendException(NotFriendException exception) {
        String message = exception.getMessage();
        log.warn("{}NotFriendException: {}{}", YELLOW_COLOR_LOG, ORIGINAL_COLOR_LOG, message);
        return new ErrorMessage(200, message);
    }

    @ExceptionHandler(UserAlreadyLikedThisFilm.class)
    public ErrorMessage handleUserAlreadyLikedThisFilmException(UserAlreadyLikedThisFilm exception) {
        String message = exception.getMessage();
        log.warn("{}UserAlreadyLikedThisFilmException: {}{}", YELLOW_COLOR_LOG, ORIGINAL_COLOR_LOG, message);
        return new ErrorMessage(200, message);
    }

    @ExceptionHandler(LikeDoesNotExist.class)
    public ErrorMessage handleLikeDoesNotExistException(LikeDoesNotExist exception) {
        String message = exception.getMessage();
        log.warn("{}LikeDoesNotExistException: {}{}", YELLOW_COLOR_LOG, ORIGINAL_COLOR_LOG, message);
        return new ErrorMessage(200, message);
    }
}