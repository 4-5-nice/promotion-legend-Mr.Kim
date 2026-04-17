package com.wanted.legendkim.domain.questionboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionBoardDTO {

    private Long id;
    private String title;
    private String authorName;
    private String authorRank;
    private String createdAt;
    private boolean solved;
}
