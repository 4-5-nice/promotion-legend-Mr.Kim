package com.wanted.legendkim.domain.enrollment.dto;
// 수강 신청 결과를 JSON 으로 내려주는 응답 전용 DTO
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

    /* comment.
        아래의 두 메서드는 alreadyEnrolled 플래그 값만 다르다.
        처음 신청한 경우는 of() false로 구성했다. 이미 신청한 코스를
        또 신청한 경우에는 ofDuplicate() 로 true를 채운다.
        생성자에 true, false 를 직접 넘기는 것보다 메서드 이름에
        의도를 담는 것이 가독성이 좋아 2개로 분리.
     */

    // userId : 요청한 본인이라 굳이 필요없음
    // finishDate : 수강 신청 시점에는 아직 없기 때문에 필요없음.

    // 처음 신청 성공했을 때
    public static EnrollmentResponse of(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(), //enrollmentId
                enrollment.getCourse().getTitle(), // courseTitle (Course 를 거쳐서 꺼내기)
                enrollment.getStartAt(), // startAt
                enrollment.getDeadLineDate(), // deadLineDate
                enrollment.getStatus(), // alreadyEnrolled
                false
        );
    }

    // 이미 신청한 코스를 다시 신청했을 경우
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
