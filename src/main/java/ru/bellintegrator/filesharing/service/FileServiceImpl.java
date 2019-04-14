package ru.bellintegrator.filesharing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.model.UserFile;
import ru.bellintegrator.filesharing.repository.UserFileRepository;
import ru.bellintegrator.filesharing.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Service
public class FileServiceImpl implements FileService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserFileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileServiceImpl(UserFileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Iterable<UserFile> findAllFiles() {
        return fileRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void uploadFile(User currentUser, MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String resultFilename = createUniqueFileName(file.getOriginalFilename());

        file.transferTo(new File(uploadPath + "/" + resultFilename));

        UserFile userFile = new UserFile();
        userFile.setFileName(resultFilename);
        userFile.setUser(currentUser);
        userFile.setDownloadCount(0);
        fileRepository.save(userFile);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ResponseEntity downloadFile(UserFile userFile) throws IOException {

        File file = new File(uploadPath + "/" + userFile.getFileName());
        userFile.setDownloadCount(userFile.getDownloadCount() + 1);
        fileRepository.save(userFile);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteFile(User currentUser, String fileId) {
        Optional<UserFile> optionalUserFile = fileRepository.findById(Integer.valueOf(fileId));
        if (optionalUserFile.isPresent()) {
            UserFile fileFromDB = optionalUserFile.get();

            if (fileFromDB.getUser().equals(currentUser) || currentUser.isAdmin()) {
                File fileFromDisk = new File(uploadPath + "/" + fileFromDB.getFileName());
                fileFromDisk.delete();
                fileRepository.delete(fileFromDB);
            }
        }
    }

    /**
     * Сформировать уникальное имя файла
     *
     * @param originalFilename оригинальное имя файла
     * @return уникальное имя файла
     */
    private String createUniqueFileName(String originalFilename){
        String uuidFile = UUID.randomUUID().toString();
        return uuidFile + "." + originalFilename;
    }
}
