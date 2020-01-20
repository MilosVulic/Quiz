package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.UserQuestionAnswer;
import com.milos.vulic.quiz.repositories.UserQuestionAnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQuestionAnswerImp implements UserQuestionAnswerService{

    private final UserQuestionAnswerRepository userQuestionAnswerRepository;

    public UserQuestionAnswerImp(UserQuestionAnswerRepository userQuestionAnswerRepository) {
        this.userQuestionAnswerRepository = userQuestionAnswerRepository;
    }

    @Override
    public void insertUserQuestionAnswer(UserQuestionAnswer userQuestionAnswer) {
        userQuestionAnswerRepository.save(userQuestionAnswer);
    }

    @Override
    public UserQuestionAnswer findByQuestionIdAndUserId(Long questionId, Long userId) {
        return userQuestionAnswerRepository.findByQuestion_QuestionIdAndUser_UserId(questionId,userId);
    }

    @Override
    public void updateUserQuestionAnswer(UserQuestionAnswer userQuestionAnswer) {
        userQuestionAnswerRepository.save(userQuestionAnswer);
    }

    @Override
    public List<UserQuestionAnswer> findallByUserId(Long userId) {
        return userQuestionAnswerRepository.findAllByUser_UserId(userId);
    }

    @Override
    public List<UserQuestionAnswer> findAll() {
        return userQuestionAnswerRepository.findAll();
    }
}
