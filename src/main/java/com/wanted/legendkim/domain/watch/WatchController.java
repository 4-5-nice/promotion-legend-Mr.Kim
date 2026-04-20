package com.wanted.legendkim.domain.watch;

import com.wanted.legendkim.domain.watch.dto.WatchInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/enrollments")
public class WatchController {

    private final WatchService watchService;

    @GetMapping("/{enrollmentId}/watch")
    public String watch(
            @PathVariable Long enrollmentId,
            Model model) {

        WatchInfoResponse response = watchService.getWatchInfo(enrollmentId);
        model.addAttribute("watchInfo", response);
        model.addAttribute("enrollmentId", enrollmentId);
        return "movie/movie";
    }
}
