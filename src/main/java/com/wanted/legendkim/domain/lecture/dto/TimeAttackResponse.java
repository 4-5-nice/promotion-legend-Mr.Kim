package com.wanted.legendkim.domain.lecture.dto;
// 수강 마감일 정보를 내려주는 응답 DTO. 만료 여부까지 함께 계산해서 포함.
import com.wanted.legendkim.domain.enrollment.entity.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// 정적 팩토리만 생성
@AllArgsConstructor
public class TimeAttackResponse {
    // 수강 D-day 정보를 내려주는 응답 DTO.
    // 만료 여부까지 함께 계산해서 포함.
    private Long enrollmentId;
    private LocalDateTime deadLineDate;
    private boolean isExpired;

    // 시간에 따라 바뀌는 값을 DB에 저장하지 않고 응답 시점에 계산한다.
    public static TimeAttackResponse of(Enrollment enrollment) {
        boolean expired = enrollment.getDeadLineDate().isBefore(LocalDateTime.now());
        return new TimeAttackResponse(
                enrollment.getId(),
                enrollment.getDeadLineDate(),
                expired
        );
    }
}
