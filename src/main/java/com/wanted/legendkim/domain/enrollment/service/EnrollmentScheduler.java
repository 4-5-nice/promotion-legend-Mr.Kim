package com.wanted.legendkim.domain.enrollment.service;

import com.wanted.legendkim.domain.enrollment.entity.Enrollment;
import com.wanted.legendkim.domain.enrollment.dao.EnrollmentRepository;
import com.wanted.legendkim.domain.enrollment.entity.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j // lombok 에서 log 변수 자동 주입하는 어노테이션
@Component // 스프링이 관리하는 빈으로 등록
@RequiredArgsConstructor // final 필드 생성자 주입
public class EnrollmentScheduler {

    // final 으로 선언
    private final EnrollmentRepository enrollmentRepository;

    // 매일 자정에 실행 — deadLineDate 지난 IN_PROGRESS 수강 자동 만료
    @Scheduled(cron = "0 0 0 * * *")
    // 메서드 전체를 하나의 트랜젝션으로 묶기
    @Transactional
    public void expireOverdueEnrollments() {
        // 진행중 인데 마감일이 지금 이전인 것들을 일괄적으로 조회한다.
        // Completed, expired 는 이미 끝난 상태라 제외함
        List<Enrollment> targets = enrollmentRepository
                .findAllByStatusAndDeadLineDateBefore(EnrollmentStatus.IN_PROGRESS, LocalDateTime.now());

        // 엔티티의 expire() 메서드 호출 후 EXPIRED 로 변경한다.
        for (Enrollment enrollment : targets) {
            enrollment.expire();
        }

        /* comment.
            SLF4J 는 로그 출력 인터페이스이며, LogBack 은 그 구현체이다. SpringBoot
            에서 @Slf4j 를 붙이면 log.info() 호출이 SLF4J 를 거쳐서 LogBack 이
            실제로 처리하는 구조이다. 이렇게 나눈 덕에 로그 라이브러리 비즈니스 코드는
            바꾸지 않아도 되는 장점이 있다.
         */
          /* comment.
              몇 건 처리했는지 로그로 남긴다.
              나중에 log.info 는 SLF4J + logback 이 받아서 출력한다.
         */
        log.info("[스케줄러] 타임어택 만료 처리 완료 — 총 {}건", targets.size());
    }
}
