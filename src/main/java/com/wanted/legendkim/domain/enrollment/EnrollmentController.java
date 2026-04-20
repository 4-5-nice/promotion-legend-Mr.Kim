package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.enrollment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 반환 값 JSON으로 내려주기
@RequiredArgsConstructor // 필드 생성자 주입
@RequestMapping("/user/enrollments")
public class EnrollmentController {

    // 서비스 레이어 주입, 변경 불가능하게 final 사용
    private final EnrollmentService enrollmentService;

    // 수강 신청 처리
    // 서비스가 DTO 를 받아 Enrollment 생성 후 EnrollmentResponse 로 변환해 반환
    @PostMapping("/")
    public ResponseEntity<EnrollmentResponse> enrollment(@RequestBody
                                                         EnrollmentRequest request){
        EnrollmentResponse response = enrollmentService.enrollment(request);
        return ResponseEntity.ok(response);
    }

    // 내 수강목록 조회
    @GetMapping("/")
    // 쿼리 파라미터로 userId 받음.
    public ResponseEntity<List<EnrollmentSummary>> getMyEnrollments(@RequestParam
                                                                    Long userId) {
        // 응답 타입이 List<EnrollmentSummary> -> 목록용이라 요약 DTO 사용.
        List<EnrollmentSummary> list = enrollmentService.getMyEnrollments(userId);
        return ResponseEntity.ok(list);
    }

    // 진행률 조회
    @GetMapping("/{enrollmentId}/progress")
    // URL 경로에서 값 추출
    public ResponseEntity<ProgressResponse> getProgress(@PathVariable Long enrollmentId) {
        // 진행률 전용 DTO
        ProgressResponse response = enrollmentService.getProgress(enrollmentId);
        return ResponseEntity.ok(response);
    }

    // 진행률 업데이트
    @PatchMapping("/{enrollmentId}/progress") // 진행률 갱신
    // @PathVariable + @RequsetBody 혼합 사용 -> 식별자는 URL
    public ResponseEntity<String> updateProgress(@PathVariable Long enrollmentId,
                                                 @RequestBody ProgressRequest request) {
        enrollmentService.updateProgress(enrollmentId, request.getProgress());
        // 성공 시 성공 문구 반환
        return ResponseEntity.ok("수강률 업데이트 완료");
    }

    // 수강 완료 - patch 사용 이유는 리소스의 일부 상태를 바꾸는 것이기 때문
    @PatchMapping("/{enrollmentId}/complete") // 수강 완료 처리
    // 어떤 수강 건을 완료 처리할지만 알면 된다.
    public ResponseEntity<String> complete(@PathVariable Long enrollmentId) {
        enrollmentService.complete(enrollmentId);
        return ResponseEntity.ok("수강 완료 처리되었습니다.");
    }
}