package ru.bellintegrator.filesharing.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.model.UserFile;

import java.io.IOException;

/**
 * Сервис файлов
 */
public interface FileService {

    /**
     * Найти все файлы в системе
     *
     * @return список файлов
     */
    Iterable<UserFile> findAllFiles();

    /**
     * Добавить файл в систему
     *
     * @param currentUser текущий пользователь
     * @param file файл
     */
    void uploadFile(User currentUser, MultipartFile file) throws IOException;

    /**
     * Загрузить файл из системы
     *
     * @param userFile файл
     * @return ResponseEntity сформированный ответ для контроллера
     */
    ResponseEntity downloadFile(UserFile userFile) throws IOException;

    /**
     * Удалить файл
     *
     * @param currentUser текущий пользователь
     * @param fileId
     */
    void deleteFile(User currentUser, String fileId);

}
