package ru.yauroff.awsfileloader.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsfileloader.model.*;
import ru.yauroff.awsfileloader.service.FileService;
import ru.yauroff.awsfileloader.service.UserService;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.util.List;


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
    public ResponseEntity<List<File>> getAllFiles() {
        List<File> fileList = fileService.getAll();
        if (fileList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('files:write')")
    public @ResponseBody
    ResponseEntity<File> uploadFile(@RequestParam("name") String name,
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

        Event event = new Event();
        event.setUser(user);
        event.setFile(fileEntity);
        event.setActionType(ActionType.LOAD);

        try {
            fileEntity = this.fileService.uploadFile(fileEntity, event, multipartFile);
        } catch (IOException e) {
            new ResponseEntity<>("Error upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(fileEntity, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('files:read')")
    public ResponseEntity<File> getFile(@PathVariable("id") Long fileId) {
        if (fileId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        File file = fileService.getById(fileId);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('files:read')")
    public ResponseEntity<Long> getCountFiles() {
        Long count = this.fileService.getCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


}
