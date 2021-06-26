package ru.yauroff.awsfileloader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "file")
@Getter
@Setter
@ToString
public class File extends BaseEntity {
    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "fileLocation")
    private String fileLocation;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private FileStatus status;
}
