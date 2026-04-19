package com.wanted.legendkim.domain.enrollment.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EnrollmentRequest {

    private Long userId;
    private Long courseId;
    private String userTrack; // 유저 직급 — 수강 권한 체크용

}
