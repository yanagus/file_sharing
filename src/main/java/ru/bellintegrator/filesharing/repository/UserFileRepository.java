package ru.bellintegrator.filesharing.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bellintegrator.filesharing.model.UserFile;

import java.util.List;

/**
 * Репозиторий для работы с файлами
 */
public interface UserFileRepository extends CrudRepository<UserFile, Integer> {

}
