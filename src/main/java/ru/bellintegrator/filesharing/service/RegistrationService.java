package ru.bellintegrator.filesharing.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.bellintegrator.filesharing.model.User;

/**
 * Сервис регистрации пользователя
 */
public interface RegistrationService extends UserDetailsService {

    /**
     * Добавить нового пользователя
     *
     * @param user пользователь
     */
    void addUser(User user);

    /**
     * Активировать пользователя
     *
     * @param code код активации
     */
    void activateUser(String code);
}
