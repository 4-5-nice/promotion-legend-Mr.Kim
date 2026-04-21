package com.wanted.legendkim.domain.lecture.controller;

import com.wanted.legendkim.domain.lecture.dto.LectureResponse;
import com.wanted.legendkim.domain.lecture.dto.TimeAttackResponse;
import com.wanted.legendkim.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LectureController {

    // 서비스 레이어 주입 - 컨트롤러는 HTTP 변환만 진행
    private final LectureService lectureService;

    // GET /user/lectures/{lectureId}
    // 강의 상세 조회 로직
    @GetMapping("/user/lectures/{lectureId}")
    public ResponseEntity<LectureResponse> getLecture(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getLecture(lectureId));
    }

    // GET /user/lectures/{lectureId}/time-attack
    // 타임 어택 D-Day 조회
    @GetMapping("/user/lectures/{lectureId}/time-attack")
    public ResponseEntity<TimeAttackResponse> getTimeAttack(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getTimeAttack(lectureId));
    }

    // PATCH /lectures/{lectureId}/time-attack/expire
    // 타임어택 수동 만료
    @PatchMapping("/lectures/{lectureId}/time-attack/expire")
    public ResponseEntity<String> expireTimeAttack(@PathVariable Long lectureId) {
        lectureService.expireTimeAttack(lectureId);
        return ResponseEntity.ok("타임어택이 만료되었습니다.");
    }
}
