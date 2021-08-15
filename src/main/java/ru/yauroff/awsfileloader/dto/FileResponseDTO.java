package ru.yauroff.awsfileloader.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yauroff.awsfileloader.model.File;
import ru.yauroff.awsfileloader.model.FileStatus;

@AllArgsConstructor
@Getter
@Setter
public class FileResponseDTO {
    private Long id;
    private String name;
    private String fileLocation;
    private String description;
    private FileStatus status;

    public static FileResponseDTO fromFile(File file) {
        FileResponseDTO fileResponseDTO = new FileResponseDTO(file.getId(),
                file.getName(), file.getFileLocation(), file.getDescription(), file.getStatus());
        return fileResponseDTO;
    }
}
