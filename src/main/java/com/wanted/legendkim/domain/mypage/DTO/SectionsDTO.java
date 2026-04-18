package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SectionsDTO {
    private int sectionId;
    private int courseId;
    private String title;
    private String videoUrl;
    private boolean uploadSuccess;
}
