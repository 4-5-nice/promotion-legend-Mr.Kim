package com.wanted.legendkim.domain.enrollment;

import com.wanted.legendkim.domain.enrollment.dto.WatchInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/{enrollmentId}/watch")
    public String watch(
            @PathVariable Long enrollmentId,
            Model model) {

        WatchInfoResponse response = enrollmentService.getWatchInfo(enrollmentId);
        model.addAttribute("watchInfo", response);
        return "movie/movie";
    }

}
