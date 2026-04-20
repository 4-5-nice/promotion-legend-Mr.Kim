package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SectionProgressDTO {
    private int progressId;
    private int enrollmentId;
    private int sectionId;
    private boolean isCompleted;
}
