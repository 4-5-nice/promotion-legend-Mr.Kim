package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentsDTO {
    private int commentId;
    private int postId;
    private int userId;
    private int questionId;
    private String content;
    private LocalDateTime createdAt;
}
