package com.wanted.legendkim.domain.enrollment.dto;

import com.wanted.legendkim.domain.enrollment.Enrollment;
import com.wanted.legendkim.domain.enrollment.EnrollmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class EnrollmentSummary {

    private Long enrollmentId;
    private String courseTitle;
    private String instructorName;
    private EnrollmentStatus status;
    private LocalDateTime deadLineDate;

    // 원본 Enrollment Entity 를 파라미터로 받아 필요한 값만 조합한다.
    public static EnrollmentSummary of(Enrollment enrollment) {
        return new EnrollmentSummary(
                enrollment.getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getInsName(),
                enrollment.getStatus(),
                enrollment.getDeadLineDate()
        );
    }
}
