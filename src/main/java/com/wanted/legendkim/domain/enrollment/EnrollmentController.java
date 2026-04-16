package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.enrollment.dto.WatchInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/{enrollmentId}/watch")
    public ResponseEntity<WatchInfoResponse> watch(
            @PathVariable Long enrollmentId) {

        WatchInfoResponse response = enrollmentService.getWatchInfo(enrollmentId);
        return ResponseEntity.ok(response);
    }

}
