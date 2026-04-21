package com.wanted.legendkim.domain.watch.service;

import com.wanted.legendkim.domain.enrollment.entity.Enrollment;
import com.wanted.legendkim.domain.enrollment.dao.EnrollmentRepository;
import com.wanted.legendkim.domain.section.dao.SectionRepository;
import com.wanted.legendkim.domain.users.user.model.dao.UserRepository;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import com.wanted.legendkim.domain.watch.dto.SectionSummary;
import com.wanted.legendkim.domain.watch.dto.WatchInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchService {

    // 수강 정보를 조회하기 위해 필요 — 코스 제목, 강사 이름, 연결된 코스 정보를 가져온다.
    private final EnrollmentRepository enrollmentRepository;
    // 수강 중인 코스의 섹션 목록을 가져오기 위해 필요하다.
    private final SectionRepository sectionRepository;
    // 직급별 재생 속도 제한 적용을 위해 수강자의 User 정보를 조회하는 데 필요하다.
    private final UserRepository userRepository;

    // enrollmentId 로 수강 정보를 찾고, 해당 코스의 섹션 목록을 조회해서 시청
    // 페이지용 응답 객체 하나로 조립
    public WatchInfoResponse getWatchInfo(Long enrollmentId) {

        // 수강할 정보를 찾을 수 없는 경우.
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강하시려는 정보를 찾을 수 없습니다... id : " + enrollmentId));

        List<SectionSummary> sections = sectionRepository
                // 조회된 Enrollment 에서 연결된 Course 객체를 탐색해서 CourseId 를 꺼낸다.
                .findByCourseId(enrollment.getCourse().getId())
                .stream()
                // 각 Section 엔티티를 SectionSummary DTO 로 변환
                .map(SectionSummary::of)
                .toList();

        // Enrollment 에 저장된 userId 로 User 를 조회한다.
        // 직급(rank) 정보를 꺼내 재생 속도 제한 값을 프론트에 전달하기 위함이다.
        User user = userRepository.findById(enrollment.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. userId : " + enrollment.getUserId()));

        // 수강 정보, 섹션 목록, 사용자 직급을 하나의 응답 DTO 로 조립 후 반환
        return WatchInfoResponse.of(enrollment, sections, user.getRank());
    }
}
