package ru.yauroff.awsfileloader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yauroff.awsfileloader.model.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
