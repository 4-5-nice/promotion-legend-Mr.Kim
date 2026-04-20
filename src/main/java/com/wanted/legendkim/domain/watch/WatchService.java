package com.wanted.legendkim.domain.watch;

import com.wanted.legendkim.domain.enrollment.Enrollment;
import com.wanted.legendkim.domain.enrollment.EnrollmentRepository;
import com.wanted.legendkim.domain.section.SectionRepository;
import com.wanted.legendkim.domain.watch.dto.SectionSummary;
import com.wanted.legendkim.domain.watch.dto.WatchInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchService {

    // 수강 정보를 조회해야하는 코스 제목, 강사 이름, 어떤 코스인지를 알 수 있기 떄문에 필요하다.
    private final EnrollmentRepository enrollmentRepository;
    // 수강 중인 코스의 섹션 목록을 가져오기 위해 필요하다.
    private final SectionRepository sectionRepository;

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

        // 수강 정보와 섹션 리스트 두 가지를 합쳐서 최종 응답 DTO 하나로 조립 후 반환
        return WatchInfoResponse.of(enrollment, sections);
    }
}
