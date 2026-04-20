package com.wanted.legendkim.domain.enrollment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentScheduler {

    private final EnrollmentRepository enrollmentRepository;

    // 매일 자정에 실행 — deadLineDate 지난 IN_PROGRESS 수강 자동 만료
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireOverdueEnrollments() {
        List<Enrollment> targets = enrollmentRepository
                .findAllByStatusAndDeadLineDateBefore(EnrollmentStatus.IN_PROGRESS, LocalDateTime.now());

        for (Enrollment enrollment : targets) {
            enrollment.expire();
        }

        log.info("[스케줄러] 타임어택 만료 처리 완료 — 총 {}건", targets.size());
    }
}
