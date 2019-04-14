package ru.bellintegrator.filesharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bellintegrator.filesharing.model.Role;
import ru.bellintegrator.filesharing.model.RoleName;

/**
 * Репозиторий для работы с ролью пользователя
 */
public interface RoleRepository extends JpaRepository<Role, Byte> {

    /**
     * Найти роль
     *
     * @param roleName название роли пользователя
     * @return роль
     */
    Role findByRoleName(RoleName roleName);
}
