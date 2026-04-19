package com.wanted.legendkim.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionCommentDTO {

    private Long id;
    private String authorName;
    private String content;
    private String createdAt;
    private boolean mine;
}
