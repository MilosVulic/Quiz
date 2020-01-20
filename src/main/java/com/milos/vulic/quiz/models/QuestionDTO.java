package com.milos.vulic.quiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    private Long questionId;
    private String question;
    private Integer numberOfCorrectAnse;
}
