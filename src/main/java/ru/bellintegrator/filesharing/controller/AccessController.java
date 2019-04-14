package ru.bellintegrator.filesharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bellintegrator.filesharing.exception.EntityNotFoundException;
import ru.bellintegrator.filesharing.model.Access;
import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.model.UserFile;
import ru.bellintegrator.filesharing.service.AccessService;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Контроллер, обрабатывающий запросы на доступ к файлам
 */
@Controller
public class AccessController {

    private final AccessService accessService;

    @Autowired
    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    /**
     * Запросить доступ у другого пользователя на просмотр списка его файлов
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, у которого запрашивается доступ
     * @return страница со списком файлов пользователя
     */
    @GetMapping("/askread/{user}")
    public String askRead(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
        accessService.saveRequestToRead(user, currentUser);
        return "redirect:/user-files/" + user.getId();
    }

    /**
     * Запросить доступ у другого пользователя на скачивание его файлов
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, у которого запрашивается доступ
     * @return страница со списком файлов пользователя
     */
    @GetMapping("/askdownload/{user}")
    public String askDownload(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
        accessService.saveRequestToDownload(user, currentUser);
        return "redirect:/user-files/" + user.getId();
    }

    /**
     * Показать список подписчиков, запрашивающих доступ
     *
     * @param currentUser текущий пользователь
     * @param model модель
     * @return список подписчиков, запрашивающих доступ
     */
    @GetMapping("/subscribers")
    public String showSubscribers(@AuthenticationPrincipal User currentUser, Model model) {
        Set<Access> accesses = accessService.getRequestingAccesses(currentUser);
        model.addAttribute("accesses", accesses);
        return "subscribers";
    }

    /**
     * Разрешить просмотр файлов пользователю
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, запрашивающий доступ
     * @return список подписчиков, запрашивающих доступ
     */
    @GetMapping("/allowRead/{user}")
    public String allowRead(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
        accessService.allowReadOrDownload(currentUser, user);
        return "redirect:/subscribers";
    }

    /**
     * Разрешить скачивание файлов пользователю
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, запрашивающий доступ
     * @return список подписчиков, запрашивающих доступ
     */
    @GetMapping("/allowDownload/{user}")
    public String allowDownload(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
        accessService.allowReadOrDownload(currentUser, user);
        return "redirect:/subscribers";
    }

    /**
     * Отобразить файлы пользователя
     *
     * @param currentUser текущий пользователь
     * @param fileOwner владелец файлов
     * @param model модель
     * @param file файл
     * @return страница со списком файлов запрашиваемого пользователя
     */
    @GetMapping("/user-files/{fileOwner}")
    public String showUserFiles(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User fileOwner,
            Model model,
            @RequestParam(required = false) UserFile file
    ) {
        if (fileOwner == null) {
            throw new EntityNotFoundException("User not found!");
        }

        if(isAdminOrAnalystOrFileOwner(currentUser, fileOwner)) {
            model.addAttribute("readAccess", false);
            return addAttributes(model, fileOwner.getFiles(), fileOwner, file, isFileOwner(currentUser, fileOwner),
                    true, null);
        }

        Optional<Access> optionalAccess = getOptionalAccess(fileOwner, currentUser);
        if (optionalAccess.isPresent()){
            Access access = optionalAccess.get();
            if(!access.getRequest() && access.getDownloadAccess()) {
                model.addAttribute("readAccess", false);
                return addAttributes(model, fileOwner.getFiles(), fileOwner, file, isFileOwner(currentUser, fileOwner),
                        isAdminOrAnalystOrFileOwner(currentUser, fileOwner), null);
            }
            if((!access.getRequest() && access.getReadAccess() && !access.getDownloadAccess())
                    || (access.getRequest() && access.getReadAccess() && access.getDownloadAccess())) {
                model.addAttribute("readAccess", true);
                return addAttributes(model, fileOwner.getFiles(), fileOwner, file, isFileOwner(currentUser, fileOwner),
                        isAdminOrAnalystOrFileOwner(currentUser, fileOwner), null);
            }
            return addAttributes(model, Collections.EMPTY_SET, fileOwner, file, isFileOwner(currentUser, fileOwner),
                    isAdminOrAnalystOrFileOwner(currentUser, fileOwner), "No files available for you");
        }
        return addAttributes(model, Collections.EMPTY_SET, fileOwner, file, isFileOwner(currentUser, fileOwner),
                isAdminOrAnalystOrFileOwner(currentUser, fileOwner), "No files available for you");
    }

    /**
     * Получить объект Optional отношений доступа владельца файлов и текущего пользователя
     *
     * @param fileOwner владелец файлов
     * @param currentUser текущий пользователь
     * @return объект Optional
     */
    private Optional<Access> getOptionalAccess(User fileOwner, User currentUser) {
        return fileOwner.getSubscribers()
                .stream()
                .filter(access -> access.getSubscriber().equals(currentUser))
                .findAny();
    }

    /**
     * Добавить атрибуты в модель
     *
     * @param model модель
     * @param files список файлов
     * @param fileOwner владелец файлов
     * @param file выбранный файл
     * @param isFileOwner true, если пользователь - владелец файлов
     * @param isAdminOrAnalystOrFileOwner true, если пользователь - админимстратор, аналитик или владелец файлов
     * @return страница со списком файлов запрашиваемого пользователя
     */
    private String addAttributes(Model model, Set<UserFile> files, User fileOwner, UserFile file,
                                boolean isFileOwner, boolean isAdminOrAnalystOrFileOwner, String info) {
        model.addAttribute("files", files);
        model.addAttribute("fileOwner", fileOwner);
        model.addAttribute("file", file);
        model.addAttribute("isFileOwner", isFileOwner);
        model.addAttribute("isAdminOrAnalystOrFileOwner", isAdminOrAnalystOrFileOwner);
        model.addAttribute("info", info);
        return "userFiles";
    }

    /**
     * Является ли текущий пользователь админимстратором, аналитиком или владельцем файлов
     *
     * @param currentUser текущий пользователь
     * @param fileOwner владелец файлов
     * @return true, если пользователь один и тот же
     */
    private boolean isAdminOrAnalystOrFileOwner(User currentUser, User fileOwner) {
        return currentUser.isAdmin() || currentUser.isAnalyst() || isFileOwner(currentUser, fileOwner);
    }

    /**
     * Является ли текущий пользователь владельцем файлов
     *
     * @param currentUser текущий пользователь
     * @param fileOwner владелец файлов
     * @return true, если пользователь один и тот же
     */
    private boolean isFileOwner(User currentUser, User fileOwner) {
        return currentUser.getId().equals(fileOwner.getId());
    }

}
