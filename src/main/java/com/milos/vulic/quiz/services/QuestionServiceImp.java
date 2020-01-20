package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.Question;
import com.milos.vulic.quiz.repositories.QuestionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImp implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImp(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> getListOfQuestions() {
        return questionRepository.findAll(Sort.by("questionId").ascending());
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionRepository.findByQuestionId(id);
    }

    @Override
    public void addQuestion(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Override
    public void deleteQuestionById(Long id) {
        questionRepository.deleteById(id);
    }
}
