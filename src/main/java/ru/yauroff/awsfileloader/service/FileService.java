package ru.yauroff.awsfileloader.service;

import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsfileloader.model.Event;
import ru.yauroff.awsfileloader.model.File;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<File> getAll();

    File getById(Long id);

    long getCount();

    File uploadFile(File fileEntity, Event event, MultipartFile multipartFile) throws IOException;

    File update(File file);

    void deleteById(Long id);
}
