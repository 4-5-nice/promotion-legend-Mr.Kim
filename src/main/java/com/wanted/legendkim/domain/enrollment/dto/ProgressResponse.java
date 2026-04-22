package com.wanted.legendkim.domain.enrollment.dto;
// 진행률 업데이트/조회의 응답 DTO
import com.wanted.legendkim.domain.enrollment.entity.Enrollment;
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
                enrollment.getId(), // enrollmentId
                enrollment.getProgress() // progress
        );
    }

}
