package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionsDTO {
    private int questionId;
    private int userId;
    private int courseId;
    private int sectionId;
    private String title;
    private String content;
    private int answer;
    private Date createdAt;
    private int viewCount;
}
