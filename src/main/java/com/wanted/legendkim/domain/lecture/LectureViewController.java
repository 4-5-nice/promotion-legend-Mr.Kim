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

    private final LectureService lectureService;

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
