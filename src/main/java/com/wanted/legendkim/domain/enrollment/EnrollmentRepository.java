package com.wanted.legendkim.domain.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findAllByUserId(Long userId);

    List<Enrollment> findByCourseId(Long courseId);

    // 만료 대상 조회 — deadLineDate 지났고 아직 IN_PROGRESS 인 것들
    List<Enrollment> findAllByStatusAndDeadLineDateBefore(EnrollmentStatus status, LocalDateTime now);

}
