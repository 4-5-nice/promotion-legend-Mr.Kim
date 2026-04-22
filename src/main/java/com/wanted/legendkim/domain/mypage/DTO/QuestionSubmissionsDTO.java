package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionSubmissionsDTO {
    private int submissionId;
    private int questionId;
    private int userId;
    private Date submittedAt;
    private boolean isCorrect;

//    private int selectedAnswer;
}
