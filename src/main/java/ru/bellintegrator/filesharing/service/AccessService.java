package ru.bellintegrator.filesharing.service;

import ru.bellintegrator.filesharing.model.Access;
import ru.bellintegrator.filesharing.model.User;

import java.util.Set;

/**
 * Сервис доступа к файлам
 */
public interface AccessService {

    /**
     * Сохранить запрос на чтение файлов
     *
     * @param user пользователь
     * @param subscriber подписчик
     */
    void saveRequestToRead(User user, User subscriber);

    /**
     * Сохранить запрос на запись файлов
     *
     * @param user пользователь
     * @param subscriber подписчик
     */
    void saveRequestToDownload(User user, User subscriber);

    /**
     * Получить список доступов с подписчиками, запрашивающими доступ
     *
     * @param currentUser текущий пользователь
     * @return список доступов
     */
    Set<Access> getRequestingAccesses(User currentUser);

    /**
     * Разрешить просмотр или скачивание файлов подписчику
     *
     * @param currentUser текущий пользователь
     * @param subscriber подписчик
     */
    void allowReadOrDownload(User currentUser, User subscriber);

    /**
     * Получить доступ по пользователю и подписчику из базы данных
     *
     * @param user пользователь
     * @param subscriber подписчик
     * @return доступ
     */
    Access findByUserAndSubscriber(User user, User subscriber);

}
