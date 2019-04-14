package ru.bellintegrator.filesharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.filesharing.exception.EntityNotFoundException;

import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.model.UserFile;
import ru.bellintegrator.filesharing.service.FileService;

import java.io.IOException;
import java.util.Map;

/**
 * Контроллер файлов
 */
@Controller
public class FileController {

    @Value("${upload.path}")
    private String uploadPath;

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Отобразить все файлы в системе.
     * Страница доступна только администратору и аналитику
     *
     * @param model модель
     * @return страница со списком файлов
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ANALYST')")
    @GetMapping("/files")
    public String showAllFiles(Model model) {
        Iterable<UserFile> files = fileService.findAllFiles();

        model.addAttribute("readAccess", false);
        model.addAttribute("files", files);

        return "files";
    }

    /**
     * Добавить файл в систему
     *
     * @param currentUser текущий пользователь
     * @param model модель
     * @param file файл
     * @return страница со списком файлов
     * @throws IOException
     */
    @PostMapping("/user-files/{user}")
    public String uploadFile(
            @AuthenticationPrincipal User currentUser,
            Map<String, Object> model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new EntityNotFoundException("Select file!");
        }

        fileService.uploadFile(currentUser, file);
        return "redirect:/user-files/" + currentUser.getId();
    }

    /**
     * Загрузить файл из системы
     *
     * @param userFile файл
     * @return ResponseEntity сформированный ответ контроллера
     * @throws IOException
     */
    @GetMapping("/download/{userFile}")
    public ResponseEntity downloadFile (@PathVariable UserFile userFile) throws IOException {
        if (userFile == null) {
            throw new EntityNotFoundException("File not found!");
        }
        return fileService.downloadFile(userFile);
    }

    /**
     * Удалить файл из системы
     *
     * @param currentUser текущий пользователь
     * @param fileId id файла
     * @return страница со списком файлов
     */
    @GetMapping("/delete/{fileId}")
    public String deleteFile(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String fileId
    ) {
        if (fileId == null) {
            throw new EntityNotFoundException("File not found!");
        }
        fileService.deleteFile(currentUser, fileId);
        return "redirect:/user-files/" + currentUser.getId();
    }



}
