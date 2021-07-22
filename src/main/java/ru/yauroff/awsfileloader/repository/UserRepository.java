package ru.yauroff.awsfileloader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yauroff.awsfileloader.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
