package com.wanted.legendkim.domain.lecture;

import com.wanted.legendkim.domain.enrollment.Enrollment;
import com.wanted.legendkim.domain.enrollment.EnrollmentRepository;
import com.wanted.legendkim.domain.lecture.dto.LectureResponse;
import com.wanted.legendkim.domain.lecture.dto.TimeAttackResponse;
import com.wanted.legendkim.domain.section.Section;
import com.wanted.legendkim.domain.section.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본 : 읽기 전용
public class LectureService {

    // Section DB 에 접근해 객체를 주입받을 자리 선언
    private final SectionRepository sectionRepository;
    // Enrollment DB 접근해 객체를 주입받을 자리를 선언
    private final EnrollmentRepository enrollmentRepository;

    // GET /user/lectures/{lectureId}
    // LectureId 로 강의를 찾고, 없으면 예외를 던지고, 있으면 응답 DTO 로 변환한다. 강의 상세 페이지 용도
    public LectureResponse getLecture(Long lectureId) {
        Section section = sectionRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. id: " + lectureId));
        return LectureResponse.of(section);
    }

    // GET /user/lectures/{lectureId}/time-attack
    // 해당 코스의 수강 중 하나를 찾아 D-day/만료 여부를 담은 것을 DTO 로 만들어 변환한다.
    public TimeAttackResponse getTimeAttack(Long lectureId) {
        Section section = sectionRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. id: " + lectureId));

        Enrollment enrollment = enrollmentRepository.findByCourseId(section.getCourse().getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다."));

        return TimeAttackResponse.of(enrollment);
    }

    // PATCH /lectures/{lectureId}/time-attack/expire
    // 해당 강의 수강을 강제로 만료 처리시킨다.
    @Transactional
    public void expireTimeAttack(Long lectureId) {
        Section section = sectionRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. id: " + lectureId));

        Enrollment enrollment = enrollmentRepository.findByCourseId(section.getCourse().getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("수강 정보를 찾을 수 없습니다."));

        enrollment.expire();
    }
}
