package ru.yauroff.awsfileloader.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsfileloader.model.File;
import ru.yauroff.awsfileloader.model.User;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<File> getAll();

    File getById(Long id);

    long getCount();

    File uploadFile(MultipartFile multipartFile, File fileEntity, User user) throws IOException;

    Resource downloadFile(File fileEntity, User user) throws IOException;

    void deleteFile(File fileEntity, User user) throws IOException;
}
