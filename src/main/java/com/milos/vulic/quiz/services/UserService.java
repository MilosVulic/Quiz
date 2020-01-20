package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void registerUser(User user);

    User findUserByCredentials(String username, String password);

    User findByUsername(String username);

    User findByUserId(Long userId);
}
