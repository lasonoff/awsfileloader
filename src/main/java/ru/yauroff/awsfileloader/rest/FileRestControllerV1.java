package ru.yauroff.awsfileloader.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsfileloader.dto.FileResponseDTO;
import ru.yauroff.awsfileloader.model.File;
import ru.yauroff.awsfileloader.model.FileStatus;
import ru.yauroff.awsfileloader.model.User;
import ru.yauroff.awsfileloader.service.FileService;
import ru.yauroff.awsfileloader.service.UserService;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private MultipartConfigElement multipartConfigElement;

    @GetMapping
    @PreAuthorize("hasAuthority('files:read')")
    public ResponseEntity<List<FileResponseDTO>> getAllFiles() {
        List<File> fileList = fileService.getAll();
        if (fileList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<FileResponseDTO> fileResponseDTOList = fileList.stream()
                                                            .map(file -> FileResponseDTO.fromFile(file))
                                                            .collect(Collectors.toList());
        return new ResponseEntity<>(fileResponseDTOList, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('files:write')")
    public @ResponseBody
    ResponseEntity<FileResponseDTO> uploadFile(@RequestParam("name") String name,
                                               @RequestParam("location") String location,
                                               @RequestParam("description") String description,
                                               @RequestParam("file") MultipartFile multipartFile,
                                               Authentication authentication) {
        if (multipartFile.isEmpty() || name == null || location == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getByLogin(authentication.getName());

        File fileEntity = new File();
        fileEntity.setFileLocation(location);
        fileEntity.setDescription(description);
        fileEntity.setName(name);
        fileEntity.setStatus(FileStatus.ACTIVE);

        try {
            fileEntity = this.fileService.uploadFile(multipartFile, fileEntity, user);
        } catch (IOException e) {
            new ResponseEntity<>("Error upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        FileResponseDTO fileResponseDTO = FileResponseDTO.fromFile(fileEntity);
        return new ResponseEntity<>(fileResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('files:read')")
    public ResponseEntity<FileResponseDTO> getFile(@PathVariable("id") Long fileId) {
        if (fileId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        File file = fileService.getById(fileId);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FileResponseDTO fileResponseDTO = FileResponseDTO.fromFile(file);
        return new ResponseEntity<>(fileResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAuthority('files:read')")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long fileId,
                                                 Authentication authentication) {
        if (fileId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        File file = fileService.getById(fileId);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userService.getByLogin(authentication.getName());
        Resource fileResource = null;
        try {
            fileResource = this.fileService.downloadFile(file, user);
        } catch (IOException e) {
            new ResponseEntity<>("Error download file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION,
                                     "attachment; filename=\"" + file.getName() + "\"")
                             .body(fileResource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('files:write')")
    public ResponseEntity<FileResponseDTO> deleteFileById(@PathVariable("id") Long fileId, Authentication authentication) {
        if (fileId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getByLogin(authentication.getName());

        File fileEntity = fileService.getById(fileId);
        if (fileEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fileEntity.setStatus(FileStatus.DELETED);

        try {
            this.fileService.deleteFile(fileEntity, user);
        } catch (IOException e) {
            new ResponseEntity<>("Error delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('files:read')")
    public ResponseEntity<Long> getCountFiles() {
        Long count = this.fileService.getCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
