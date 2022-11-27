package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractStorage<T> {
    protected Long currentId;
    private final Map<Long, T> elements;

    public AbstractStorage() {
        currentId = 0L;
        elements = new HashMap<>();
    }

    protected void saveToStorage(T element) {
        elements.put(currentId, element);
    }

    protected void updateInStorage(Long id, T element) {
        elements.put(id, element);
    }

    protected List<T> getAllElementsFromStorage(){
        return new ArrayList<>(elements.values());
    }

    protected T loadFromStorage(Long id) {
        return elements.get(id);
    }

    protected void throwNotFoundExceptionIfIdDoesNotExist(Long userId, String errorMessage) {
        if (!(elements.containsKey(userId))) {
            throw new NotFoundException(errorMessage + userId);
        }
    }
}
