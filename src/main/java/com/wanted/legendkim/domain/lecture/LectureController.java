package com.wanted.legendkim.domain.lecture;

import com.wanted.legendkim.domain.lecture.dto.LectureResponse;
import com.wanted.legendkim.domain.lecture.dto.TimeAttackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    // GET /user/lectures/{lectureId}
    @GetMapping("/user/lectures/{lectureId}")
    public ResponseEntity<LectureResponse> getLecture(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getLecture(lectureId));
    }

    // GET /user/lectures/{lectureId}/time-attack
    @GetMapping("/user/lectures/{lectureId}/time-attack")
    public ResponseEntity<TimeAttackResponse> getTimeAttack(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getTimeAttack(lectureId));
    }

    // PATCH /lectures/{lectureId}/time-attack/expire
    @PatchMapping("/lectures/{lectureId}/time-attack/expire")
    public ResponseEntity<String> expireTimeAttack(@PathVariable Long lectureId) {
        lectureService.expireTimeAttack(lectureId);
        return ResponseEntity.ok("타임어택이 만료되었습니다.");
    }
}
