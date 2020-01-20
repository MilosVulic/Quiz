package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.OfferedAnswer;

import java.util.List;

public interface OfferedAnswerService {

    List<OfferedAnswer> getListOfOfferedAnswersByQuestionId(Long id);

    List<OfferedAnswer> getAllTrueOffers();

    OfferedAnswer findCorrectAnswerByQuestionId(Long id);

    OfferedAnswer findOfferedAnswerById(Long id);

    void addOfferedAnswer(OfferedAnswer offeredAnswer);

    void deleteOfferedAnswerById(Long id);
}
