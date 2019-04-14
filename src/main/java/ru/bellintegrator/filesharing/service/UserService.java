package ru.bellintegrator.filesharing.service;

import ru.bellintegrator.filesharing.model.User;

import java.util.List;
import java.util.Map;

/**
 * Сервис пользователей
 */
public interface UserService {

    /**
     * Количество пользователей в системе
     *
     * @return количество пользователей
     */
    Long userCount();

    /**
     * Найти всех пользователей
     *
     * @return список пользователей
     */
    List<User> findAll();

    /**
     * Изменить данные пользователя
     * @param username имя пользователя
     * @param form Map ролей на форме
     * @param user пользователь
     */
    void updateUser(String username, Map<String, String> form, User user);
}
