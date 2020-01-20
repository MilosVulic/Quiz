package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.Question;

import java.util.List;

public interface QuestionService {

    List<Question> getListOfQuestions();

    Question getQuestionById(Long id);

    void addQuestion(Question question);

    void deleteQuestion(Question question);

    void deleteQuestionById(Long id);
}
