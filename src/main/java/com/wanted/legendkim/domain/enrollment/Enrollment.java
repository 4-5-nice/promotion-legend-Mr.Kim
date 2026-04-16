package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.course.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Getter
@NoArgsConstructor
public class Enrollment {

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


}
