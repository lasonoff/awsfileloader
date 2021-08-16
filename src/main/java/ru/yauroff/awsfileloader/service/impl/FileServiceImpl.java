package ru.yauroff.awsfileloader.service.impl;

import com.amazonaws.SdkClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsfileloader.model.ActionType;
import ru.yauroff.awsfileloader.model.Event;
import ru.yauroff.awsfileloader.model.File;
import ru.yauroff.awsfileloader.model.User;
import ru.yauroff.awsfileloader.repository.EventRepository;
import ru.yauroff.awsfileloader.repository.FileRepository;
import ru.yauroff.awsfileloader.service.FileService;
import ru.yauroff.awsfileloader.service.S3Service;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public File getById(Long id) {
        return fileRepository.getById(id);
    }

    @Override
    public long getCount() {
        return fileRepository.count();
    }

    @Override
    public File uploadFile(MultipartFile multipartFile, File fileEntity, User user) throws IOException {
        s3Service.putObject(multipartFile, fileEntity.getFileLocation(), fileEntity.getName());
        File file = fileRepository.save(fileEntity);
        Event event = new Event();
        event.setUser(user);
        event.setFile(fileEntity);
        event.setActionType(ActionType.UPLOAD);
        eventRepository.save(event);
        return file;
    }

    @Override
    public Resource downloadFile(File fileEntity, User user) throws IOException {
        java.io.File downloadFile = s3Service.downloadObject(fileEntity.getFileLocation(), fileEntity.getName());
        Event event = new Event();
        event.setUser(user);
        event.setFile(fileEntity);
        event.setActionType(ActionType.DOWNLOAD);
        eventRepository.save(event);
        return new FileSystemResource(downloadFile);
    }

    @Override
    public void deleteFile(File fileEntity, User user) throws IOException {
        try {
            s3Service.deleteObject(fileEntity.getFileLocation(), fileEntity.getName());
        } catch (SdkClientException e) {
            throw new IOException(e.getMessage());
        }
        fileRepository.save(fileEntity);
        Event event = new Event();
        event.setUser(user);
        event.setFile(fileEntity);
        event.setActionType(ActionType.DELETE);
        eventRepository.save(event);
    }

}
