package ru.bellintegrator.filesharing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка о том, что такая сущность не найдена
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке
     *
     * @param message сообщение
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
