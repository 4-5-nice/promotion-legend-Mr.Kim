package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.enrollment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/")
    public ResponseEntity<EnrollmentResponse> enrollment(@RequestBody
                                                         EnrollmentRequest request){
        EnrollmentResponse response = enrollmentService.enrollment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<EnrollmentSummary>> getMyEnrollments(@RequestParam
                                                                    Long userId) {
        List<EnrollmentSummary> list = enrollmentService.getMyEnrollments(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{enrollmentId}/progress")
    public ResponseEntity<ProgressResponse> getProgress(@PathVariable Long enrollmentId) {
        ProgressResponse response = enrollmentService.getProgress(enrollmentId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{enrollmentId}/progress")
    public ResponseEntity<String> updateProgress(@PathVariable Long enrollmentId,
                                                 @RequestBody ProgressRequest request) {
        enrollmentService.updateProgress(enrollmentId, request.getProgress());
        return ResponseEntity.ok("수강률 업데이트 완료");
    }

    @PatchMapping("/{enrollmentId}/complete")
    public ResponseEntity<String> complete(@PathVariable Long enrollmentId) {
        enrollmentService.complete(enrollmentId);
        return ResponseEntity.ok("수강 완료 처리되었습니다.");
    }
}