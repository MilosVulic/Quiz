package com.milos.vulic.quiz.repositories;

import com.milos.vulic.quiz.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Question findByQuestionId(Long questionId);
}
