package ru.yauroff.awsfileloader.service;

import ru.yauroff.awsfileloader.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    User getByLogin(String login);

    long getCount();

    User create(User user);

    User update(User user);

    void deleteById(Long id);
}