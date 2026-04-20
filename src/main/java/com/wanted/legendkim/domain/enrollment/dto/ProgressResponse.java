package com.wanted.legendkim.domain.enrollment.dto;

import com.wanted.legendkim.domain.enrollment.Enrollment;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProgressResponse {

    private Long enrollmentId;
    private int progress;

    public static ProgressResponse of(Enrollment enrollment) {
        return new ProgressResponse(
                enrollment.getId(),
                enrollment.getProgress()
        );
    }

}
