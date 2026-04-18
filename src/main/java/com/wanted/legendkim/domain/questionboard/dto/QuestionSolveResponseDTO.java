package com.wanted.legendkim.domain.questionboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionSolveResponseDTO {

    private boolean correct;
    private Integer correctAnswer;
    private Integer myAnswer;
}
