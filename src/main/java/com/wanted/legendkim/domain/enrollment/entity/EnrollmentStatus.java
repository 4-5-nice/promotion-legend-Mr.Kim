package com.wanted.legendkim.domain.enrollment.entity;
 // 수강 상태를 3가지 값으로 재현하는 타입 안전한 상수들의 집합
public enum EnrollmentStatus {
    IN_PROGRESS, // create() 시 기본 상태
    COMPLETED, // 사용자가 완료 처리 (complete())
    EXPIRED // 스케줄러 자동 만료 (expire())

     /* comment.
         상태는 값 자체에 집중하고, 상태 전이 로직은 Enrollment 엔티티의 책임이다.
      */

}
