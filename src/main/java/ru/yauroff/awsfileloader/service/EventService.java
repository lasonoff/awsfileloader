package ru.yauroff.awsfileloader.service;

import ru.yauroff.awsfileloader.model.Event;

import java.util.List;

public interface EventService {
    List<Event> getAll();

    Event getById(Long id);

    long getCount();

    Event create(Event event);

    Event update(Event event);

    void deleteById(Long id);

    void deleteAll();
}
