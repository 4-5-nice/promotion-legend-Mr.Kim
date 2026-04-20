package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.course.Course;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "deadline_date")
    private LocalDateTime deadLineDate;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    @Column(name = "progress")
    private int progress;


    // 객체 생성을 외부에서 직접 하지 않고, 비즈니스 규칙에 따라 생성하도록 강제.
    public static Enrollment create(Long userId, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.status = EnrollmentStatus.IN_PROGRESS;
        enrollment.userId = userId;
        enrollment.course = course;
        enrollment.deadLineDate = now()
                .plusDays(course.getDueDate());
        enrollment.startAt = now();
        return enrollment;
    }

    // 상태를 변경하는 비즈니스 로직
    public void complete() {
        this.status = EnrollmentStatus.COMPLETED; // 상태를 완료로 변경
        this.finishDate = now();
    }

    public void updateProgress(int progress) {
        this.progress = progress;
    }

    public void expire() {
        this.status = EnrollmentStatus.EXPIRED;
    }

}
