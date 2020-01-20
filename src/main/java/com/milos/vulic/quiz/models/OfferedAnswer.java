package com.milos.vulic.quiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "offered_answer")
public class OfferedAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offeredAnswerId;

    private String offeredAnswer;

    private boolean correctness;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;
}
