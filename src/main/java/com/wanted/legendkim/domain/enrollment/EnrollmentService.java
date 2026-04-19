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

    // 직급 순서 정의 — 낮을수록 하위 직급
    private static final List<String> TRACK_ORDER = List.of(
            "인턴", "계약직", "정규직", "대리", "과장", "부장", "이사", "명예퇴직"
    );

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

        // 직급 제한 체크
        String userTrack = request.getUserTrack();
        String courseTrack = course.getTrack();
        if (userTrack != null && courseTrack != null) {
            int userLevel = TRACK_ORDER.indexOf(userTrack);
            int courseLevel = TRACK_ORDER.indexOf(courseTrack);
            if (userLevel < courseLevel) {
                throw new IllegalStateException("수강 권한이 없습니다. 해당 강의는 [" + courseTrack + "] 이상만 수강 가능합니다.");
            }
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