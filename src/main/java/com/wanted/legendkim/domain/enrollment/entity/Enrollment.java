package com.wanted.legendkim.domain.enrollment.entity;
// 수강신 한 건을 표현하는 JPA 엔티티 / 자기 메소드로만 허용하는 불변 성향 엔티티
import com.wanted.legendkim.domain.course.entity.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
// 해당 클래스가 JPA 엔티티이며, DB 테이블과 매핑을 선언.
@Entity
// DB의 'enrollment' 의 이름을 가진 테이블과 연결.
@Table(name = "enrollments")
@Getter
@NoArgsConstructor
public class Enrollment {
    // Enum 타입을 DB 에 저장할 때 이름을 그대로 사용
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;

    @Id
    // MySQL 의 Auto_Increment 에 위임
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // 다른 테이블과 조인 시 어느테이블 id 인지 명확하게
    @Column(name = "enrollment_id")
    private Long id;

    // 수강하는 사용자 ID
    @Column(name = "user_id")
    private Long userId;

    // 수강 대상 코스 (N:1 연관관계) - 여러 수강신청이 하나의 코스를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    // FK 컬러명 + 코스에 없다면 수강신청이 불가능함.
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // 마감일
    @Column(name = "deadline_date")
    private LocalDateTime deadLineDate;

    // 수강 시작일 (신청 시점 기준)
    @Column(name = "start_at")
    private LocalDateTime startAt;

    // 완료일 (완료가 되기 전에는 null)
    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    // 진행률
    @Column(name = "progress")
    private int progress;


    // 객체 생성을 외부에서 직접 하지 않고, 비즈니스 규칙에 따라 생성하도록 강제.
    public static Enrollment create(Long userId, Course course) {
        Enrollment enrollment = new Enrollment();
        // 모든 Enrollment IN_PROGRESS 상태로 시작
        enrollment.status = EnrollmentStatus.IN_PROGRESS;
        enrollment.userId = userId;
        enrollment.course = course;
        enrollment.deadLineDate = now()
                .plusDays(course.getDueDate()); // 마감일 = 시작일 + 코스의 일 수
        enrollment.startAt = now(); // 시작일은 항상 지금부터
        return enrollment;
    }

    // 상태를 변경하는 비즈니스 로직
    // 수강 완료 처리 = 상태 + 완료일을 한 번에 진행
    public void complete() {
        this.status = EnrollmentStatus.COMPLETED; // 상태를 완료로 변경
        this.finishDate = now();
    }
    // 진행률 갱신 전용
    public void updateProgress(int progress) {
        this.progress = progress;
    }

    // 만료 처리
    public void expire() {
        this.status = EnrollmentStatus.EXPIRED;
    }

}
