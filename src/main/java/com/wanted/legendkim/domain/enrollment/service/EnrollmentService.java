package com.wanted.legendkim.domain.enrollment.service;

import com.wanted.legendkim.domain.course.entity.Course;
import com.wanted.legendkim.domain.course.dao.CourseRepository;
import com.wanted.legendkim.domain.enrollment.entity.Enrollment;
import com.wanted.legendkim.domain.enrollment.dao.EnrollmentRepository;
import com.wanted.legendkim.domain.enrollment.dto.EnrollmentRequest;
import com.wanted.legendkim.domain.enrollment.dto.EnrollmentResponse;
import com.wanted.legendkim.domain.enrollment.dto.EnrollmentSummary;
import com.wanted.legendkim.domain.enrollment.dto.ProgressResponse;
import com.wanted.legendkim.domain.users.user.model.dao.UserRepository;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 서비스 로직임을 의미
@Transactional // 클래스 전체 메서드에 트랜젝션 적용
@RequiredArgsConstructor // final 필드에 생성자를 주입
public class EnrollmentService {

    // 수강 관련 쿼리
    private final EnrollmentRepository enrollmentRepository;
    // 수강 신청 시 코스 존재 검증 용도
    private final CourseRepository courseRepository;
    // 수강 완료 시 포인트 적립을 위해 User 정보를 조회하는 데 필요하다.
    private final UserRepository userRepository;

    // 수강신청
    public EnrollmentResponse enrollment(EnrollmentRequest request) {
        // 코스 존재 검증
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("신청할 정보를 찾을 수 없습니다..." + request));

        // 중복 수강신청 — 기존 enrollment 반환 (alreadyEnrolled = true)
        if (enrollmentRepository.existsByUserIdAndCourseId(request.getUserId(), request.getCourseId())) {
            Enrollment existing = enrollmentRepository
                    .findByUserIdAndCourseId(request.getUserId(), request.getCourseId())
                    .orElseThrow();
            return EnrollmentResponse.ofDuplicate(existing);
        }

        // 신규 신청 생성
        // 정적 팩토리 (In_Progress 상태로 시작)
        Enrollment enrollment = Enrollment.create(request.getUserId(), course);
        enrollmentRepository.save(enrollment);
        return EnrollmentResponse.of(enrollment);
    }

    // 목록 조회
    // 클래스 레벨 @Transactional 을 덮어 사용한다.
    @Transactional(readOnly = true) // 단순 조회 전용
    public List<EnrollmentSummary> getMyEnrollments(Long userId) {
        List<Enrollment> list = enrollmentRepository.findAllByUserId(userId);
        return list.stream()
                // 메서드 레퍼런스
                // .map(enrollment -> EnrollmentSummary.of(enrollment))
                // 정적 팩토리 of() 를 컨벤션으로 통일했기 때문에 매우 깔끔해짐.
                .map(EnrollmentSummary:: of)
                .toList();
    }

    // 진행률 조회 - 스펙 재검토 대기 상태 (인수인계서)
    @Transactional(readOnly = true) // 단순 조회
    public ProgressResponse getProgress(Long enrollmentId) {
        // 없으면 예외 처리 / 만약 찾았다면 Response DTO 로 변환해 반환함.
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다. id: " + enrollmentId));
        return ProgressResponse.of(enrollment);
    }

    // 진행률 갱신
    public void updateProgress(Long enrollmentId, int progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다. id: " + enrollmentId));
        // 아래의 필드만 바꾸면 메서드 종료 시 자동으로 UPDATE
        enrollment.updateProgress(progress);
    }

    // 수강 완료 처리
    public void complete(Long enrollmentId) {
        // 수강 정보 조회 — 없으면 예외 처리
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다. id: " + enrollmentId));

        // Enrollment 상태를 COMPLETED 로 변경하고 완료일을 기록한다.
        enrollment.complete();
        enrollmentRepository.save(enrollment);

        // 수강 완료한 사용자를 조회해 포인트를 1 적립한다.
        // 포인트 변경은 User 엔티티의 earnPoint() 메서드를 통해서만 수행한다.
        User user = userRepository.findById(enrollment.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. userId: " + enrollment.getUserId()));
        user.earnPoint();
    }

}