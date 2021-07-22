package ru.yauroff.awsfileloader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yauroff.awsfileloader.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
