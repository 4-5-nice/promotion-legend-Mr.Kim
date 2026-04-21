package com.wanted.legendkim.domain.watch.controller;

import com.wanted.legendkim.domain.watch.service.WatchService;
import com.wanted.legendkim.domain.watch.dto.WatchInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/* comment.
    시청 페이지를 브라우저에 랜더링하는 역할 전담 컨트롤러이다.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/enrollments")
public class WatchController {

    // 시청 정보 조회 로직이 서비스에 있기 때문에 주입받아야한다.
    // final 로 불변을 보장
    private final WatchService watchService;

    // 수강 ID 로 시청 정보를 조회해서 Thymeleaf 모델에 담고 시청 페이지를 랜더링
    @GetMapping("/{enrollmentId}/watch")
    public String watch(
            @PathVariable Long enrollmentId,
            Model model) {

        // 실제 데이터 조회는 서비스에 위임
        WatchInfoResponse response = watchService.getWatchInfo(enrollmentId);
        model.addAttribute("watchInfo", response);
        model.addAttribute("enrollmentId", enrollmentId);
        return "movie/movie";
    }
}
