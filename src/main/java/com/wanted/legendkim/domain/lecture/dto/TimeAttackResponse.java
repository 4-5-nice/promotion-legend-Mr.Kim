package com.wanted.legendkim.domain.lecture.dto;

import com.wanted.legendkim.domain.enrollment.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TimeAttackResponse {
    private Long enrollmentId;
    private LocalDateTime deadLineDate;
    private boolean isExpired;

    public static TimeAttackResponse of(Enrollment enrollment) {
        boolean expired = enrollment.getDeadLineDate().isBefore(LocalDateTime.now());
        return new TimeAttackResponse(
                enrollment.getId(),
                enrollment.getDeadLineDate(),
                expired
        );
    }
}
