package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CoursesDTO {
    private int courseId;
    private int userId;
    private String title;
    private String instructorName;
    private String description;
    private int duedate;
}
