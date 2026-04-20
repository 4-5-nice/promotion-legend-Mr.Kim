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
@Transactional(readOnly = true)
public class LectureService {

    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;

    // GET /user/lectures/{lectureId}
    public LectureResponse getLecture(Long lectureId) {
        Section section = sectionRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. id: " + lectureId));
        return LectureResponse.of(section);
    }

    // GET /user/lectures/{lectureId}/time-attack
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
