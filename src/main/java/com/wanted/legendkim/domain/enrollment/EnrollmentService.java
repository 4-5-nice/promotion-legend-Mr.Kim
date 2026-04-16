package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.enrollment.dto.SectionSummary;
import com.wanted.legendkim.domain.enrollment.dto.WatchInfoResponse;
import com.wanted.legendkim.domain.section.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final SectionRepository sectionRepository;

    public WatchInfoResponse getWatchInfo(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강하시려는 정보를 찾을 수 없습니다... id : " + enrollmentId));

        List<SectionSummary> sections = sectionRepository
                .findByCourseId(enrollment.getCourse().getId())
                .stream()
                .map(SectionSummary :: of)
                .toList();

        return WatchInfoResponse.of(enrollment, sections);


    }

}
