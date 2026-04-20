package com.wanted.legendkim.domain.enrollment.dto;
// 내 수강목록 화면용 축약. Response DTO (리스트를 한 줄에 표시할 정보)
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

    /* comment.
        ResponseDTO를 두개로 나눈 이유.
        한 화면에 한 DTO 를 구성했다. 화면이 필요로 하는 모양에 맞춰
        DTO 를 분리했다. 하나로 합쳐서 진행하게 된다면, 목록에 필요
        없는 필드 섞여서 응답이 커진다.
     */

    // 원본 Enrollment Entity 를 파라미터로 받아 필요한 값만 조합한다.
    public static EnrollmentSummary of(Enrollment enrollment) {
        return new EnrollmentSummary(
                enrollment.getId(), // enrollmentId
                enrollment.getCourse().getTitle(), // courseTitle (Course 거침)
                enrollment.getCourse().getInsName(), // instructorName (Course 거침)
                enrollment.getStatus(), // status
                enrollment.getDeadLineDate() // deadLineDate
        );
    }
}
