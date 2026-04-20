package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.course.Course;
import com.wanted.legendkim.domain.course.CourseRepository;
import com.wanted.legendkim.domain.enrollment.dto.EnrollmentRequest;
import com.wanted.legendkim.domain.enrollment.dto.EnrollmentResponse;
import com.wanted.legendkim.domain.enrollment.dto.EnrollmentSummary;
import com.wanted.legendkim.domain.enrollment.dto.ProgressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentResponse enrollment(EnrollmentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("신청할 정보를 찾을 수 없습니다..." + request));

        // 중복 수강신청 — 기존 enrollment 반환 (alreadyEnrolled = true)
        if (enrollmentRepository.existsByUserIdAndCourseId(request.getUserId(), request.getCourseId())) {
            Enrollment existing = enrollmentRepository
                    .findByUserIdAndCourseId(request.getUserId(), request.getCourseId())
                    .orElseThrow();
            return EnrollmentResponse.ofDuplicate(existing);
        }

        Enrollment enrollment = Enrollment.create(request.getUserId(), course);
        enrollmentRepository.save(enrollment);
        return EnrollmentResponse.of(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentSummary> getMyEnrollments(Long userId) {
        List<Enrollment> list = enrollmentRepository.findAllByUserId(userId);
        return list.stream()
                .map(EnrollmentSummary:: of)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProgressResponse getProgress(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다. id: " + enrollmentId));
        return ProgressResponse.of(enrollment);
    }

    public void updateProgress(Long enrollmentId, int progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다. id: " + enrollmentId));
         enrollment.updateProgress(progress);
    }

    public void complete(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다. id: " + enrollmentId));
        enrollment.complete();
        enrollmentRepository.save(enrollment);
    }

}