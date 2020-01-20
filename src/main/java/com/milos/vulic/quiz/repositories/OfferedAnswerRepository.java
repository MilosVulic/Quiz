package com.milos.vulic.quiz.repositories;

import com.milos.vulic.quiz.models.OfferedAnswer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferedAnswerRepository extends JpaRepository<OfferedAnswer, Long> {
    List<OfferedAnswer> getAllByQuestion_QuestionId(Long questionId, Sort sort);

    OfferedAnswer findTopByQuestion_QuestionIdAndCorrectnessIsTrue(Long questionId);

    OfferedAnswer findByOfferedAnswerId(Long offeredAnswerId);

    List<OfferedAnswer> findAllByCorrectnessIsTrue();
}
