package ru.yauroff.awsfileloader.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsfileloader.model.Event;
import ru.yauroff.awsfileloader.model.File;
import ru.yauroff.awsfileloader.repository.EventRepository;
import ru.yauroff.awsfileloader.repository.FileRepository;
import ru.yauroff.awsfileloader.s3.S3Provider;
import ru.yauroff.awsfileloader.service.FileService;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private S3Provider s3Provider;

    @Override
    public List<File> getAll() {
        return null;
    }

    @Override
    public File getById(Long id) {
        return null;
    }

    @Override
    public long getCount() {
        return 0;
    }

    @Override
    public File uploadFile(File fileEntity, Event event, MultipartFile multipartFile) throws IOException {
        s3Provider.putObject(multipartFile, fileEntity.getFileLocation(), fileEntity.getName());
        File file = fileRepository.save(fileEntity);
        eventRepository.save(event);
        return file;
    }

    @Override
    public File update(File file) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
