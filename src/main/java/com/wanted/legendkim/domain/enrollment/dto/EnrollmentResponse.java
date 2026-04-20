package com.wanted.legendkim.domain.enrollment.dto;


import com.wanted.legendkim.domain.enrollment.Enrollment;
import com.wanted.legendkim.domain.enrollment.EnrollmentStatus;
import lombok.*;

import java.time.LocalDateTime;

/* comment.
    우리가 Entity 구조에서 @Setter 를 사용하지 않는 것을 학습했었다.
    하지만 여기서 @Setter 어노테이션을 사용하지 않는 이유는 "불변성"
    이라는 이유가 제일크다. DTO 는 수정할 필요 없으니 불변성의 이유로
    @Setter 어노테이션을 작성하지 않는 것이다.
    이는 Response DTO 한정이다.
 */

@NoArgsConstructor
@AllArgsConstructor
// 응답 DTO 에는 사용하지 않는다.
//@Setter
@Getter
@ToString
public class EnrollmentResponse {

    private Long enrollmentId;
    private String courseTitle;
    private LocalDateTime startAt;
    private LocalDateTime deadLineDate;
    private EnrollmentStatus status;
    private boolean alreadyEnrolled; // 중복 수강신청 여부

    public static EnrollmentResponse of(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getStartAt(),
                enrollment.getDeadLineDate(),
                enrollment.getStatus(),
                false
        );
    }

    public static EnrollmentResponse ofDuplicate(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getStartAt(),
                enrollment.getDeadLineDate(),
                enrollment.getStatus(),
                true
        );
    }

}
