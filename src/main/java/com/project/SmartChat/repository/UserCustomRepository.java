package com.project.SmartChat.repository;

import java.util.List;

import com.project.SmartChat.model.User;

public interface UserCustomRepository {
    User findByUsername(String username);
    User save(User user);

    List<User> getAllUsers();
}