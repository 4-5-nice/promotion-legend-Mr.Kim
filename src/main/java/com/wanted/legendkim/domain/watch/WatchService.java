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

    private final EnrollmentRepository enrollmentRepository;
    private final SectionRepository sectionRepository;

    public WatchInfoResponse getWatchInfo(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강하시려는 정보를 찾을 수 없습니다... id : " + enrollmentId));

        List<SectionSummary> sections = sectionRepository
                .findByCourseId(enrollment.getCourse().getId())
                .stream()
                .map(SectionSummary::of)
                .toList();

        return WatchInfoResponse.of(enrollment, sections);
    }
}
