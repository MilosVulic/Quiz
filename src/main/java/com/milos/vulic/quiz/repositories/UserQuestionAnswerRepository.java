package com.milos.vulic.quiz.repositories;

import com.milos.vulic.quiz.models.UserQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuestionAnswerRepository extends JpaRepository<UserQuestionAnswer, Long> {
    UserQuestionAnswer findByQuestion_QuestionIdAndUser_UserId(Long questionId, Long userId);

    List<UserQuestionAnswer> findAllByUser_UserId(Long userId);
}
