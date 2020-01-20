package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.UserQuestionAnswer;

import java.util.List;

public interface UserQuestionAnswerService {

    void insertUserQuestionAnswer(UserQuestionAnswer userQuestionAnswer);

    UserQuestionAnswer findByQuestionIdAndUserId(Long questionId, Long userId);

    void updateUserQuestionAnswer(UserQuestionAnswer userQuestionAnswer);

    List<UserQuestionAnswer> findallByUserId(Long userId);

    List<UserQuestionAnswer> findAll();
}
