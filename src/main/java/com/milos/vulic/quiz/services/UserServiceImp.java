package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.User;
import com.milos.vulic.quiz.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByCredentials(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }
}
