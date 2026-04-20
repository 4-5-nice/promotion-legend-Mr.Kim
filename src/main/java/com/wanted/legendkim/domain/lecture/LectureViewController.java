package com.wanted.legendkim.domain.lecture;

import com.wanted.legendkim.domain.lecture.dto.LectureResponse;
import com.wanted.legendkim.domain.lecture.dto.TimeAttackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequiredArgsConstructor
public class LectureViewController {

    // 뷰 렌더링에 필요한 데이터를 가져올 서비스 의존성 선언.
    private final LectureService lectureService;

    // 강의 상세 페이지에 표시할 데이터(강의정보 + 타임어택 + D-day) 를 모아 Model 담고,
    // Thymleaf 템플릿을 랜더링해 반환한다.
    @GetMapping("/user/lectures/{lectureId}/detail")
    public String lectureDetail(@PathVariable Long lectureId, Model model) {
        LectureResponse lecture = lectureService.getLecture(lectureId);
        TimeAttackResponse timeAttack = lectureService.getTimeAttack(lectureId);

        long dDay = ChronoUnit.DAYS.between(LocalDateTime.now(), timeAttack.getDeadLineDate());

        model.addAttribute("lecture", lecture);
        model.addAttribute("timeAttack", timeAttack);
        model.addAttribute("dDay", dDay);
        return "lecture/lectureDetail";
    }
}
