package com.wanted.legendkim.domain.questionboard.controller;

import com.wanted.legendkim.domain.questionboard.dto.QuestionBoardDTO;
import com.wanted.legendkim.domain.questionboard.dto.SectionDTO;
import com.wanted.legendkim.domain.questionboard.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questionboard/user/questionboard")
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    // 문제 게시판 페이지
    @GetMapping
    public String questionBoardPage(Model model, Principal principal) {
        String email = principal.getName();

        String myRank = questionBoardService.getMyRank(email);
        model.addAttribute("myRank", myRank);

        return "questionboard/user/questionboard";
    }

    // 문제 목록 불러오기
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<QuestionBoardDTO>> getQuestionList(
            @RequestParam String rank,
            Principal principal
    ) {
        String email = principal.getName();

        List<QuestionBoardDTO> questionList =
                questionBoardService.getQuestionList(rank, email);

        return ResponseEntity.ok(questionList);
    }

    // 문제 출제 페이지 - course 목록도 함께 전달
    @GetMapping("/write")
    public String questionWritePage(Model model) {
        model.addAttribute("courses", questionBoardService.getCourses());
        return "questionboard/user/questionboard-write";
    }

    // course 선택시 section 목록 JSON 전달
    @GetMapping("/sections")
    @ResponseBody
    public ResponseEntity<List<SectionDTO>> getSectionsByCourse(@RequestParam Long courseId) {
        List<SectionDTO> sections = questionBoardService.getSectionsByCourse(courseId);
        return ResponseEntity.ok(sections);
    }

    // 문제 출제하기
    @PostMapping("/submit")
    public String submitQuestion(
            @RequestParam String title,
            @RequestParam String option1,
            @RequestParam String option2,
            @RequestParam String option3,
            @RequestParam String option4,
            @RequestParam String option5,
            @RequestParam Integer answer,
            @RequestParam Long courseId,
            @RequestParam Long sectionId,
            Principal principal
    ) {
        String email = principal.getName();

        questionBoardService.writeQuestion(
                title,
                option1,
                option2,
                option3,
                option4,
                option5,
                answer,
                courseId,
                sectionId,
                email
        );

        return "redirect:/questionboard/user/questionboard";
    }
}
