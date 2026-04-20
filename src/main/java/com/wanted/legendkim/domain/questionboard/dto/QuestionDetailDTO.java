package com.wanted.legendkim.domain.questionboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionDetailDTO {

    private Long id;
    private String title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private Integer answer;
    private String authorName;
    private String authorRank;
    private String createdAt;
    private Long viewCount;
    private String courseTitle;
    private String sectionTitle;
    private boolean solved;
    private Integer myAnswer;
    private Boolean correct;
}
