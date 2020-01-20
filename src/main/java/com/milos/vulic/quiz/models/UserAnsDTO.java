package com.milos.vulic.quiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnsDTO implements Serializable {

    private Long offeredAnswerId;
}
