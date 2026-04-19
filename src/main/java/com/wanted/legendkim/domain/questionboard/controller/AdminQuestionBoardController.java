package com.wanted.legendkim.domain.questionboard.controller;

import com.wanted.legendkim.domain.comment.commentservice.AdminQuestionCommentService;
import com.wanted.legendkim.domain.questionboard.dto.QuestionDetailDTO;
import com.wanted.legendkim.domain.questionboard.service.AdminQuestionBoardService;
import com.wanted.legendkim.domain.questionboard.dto.QuestionBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questionboard/admin/questionboard")
public class AdminQuestionBoardController {

    private final AdminQuestionBoardService adminQuestionBoardService;
    private final AdminQuestionCommentService adminQuestionCommentService;

    @GetMapping
    public String adminQuestionBoardPage() {
        return "questionboard/admin/questionboard";
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<QuestionBoardDTO>> getQuestionList(@RequestParam String rank) {
        List<QuestionBoardDTO> questionList = adminQuestionBoardService.getQuestionList(rank);
        return ResponseEntity.ok(questionList);
    }

    @GetMapping("/{questionId}")
    public String adminQuestionDetailPage(
            @PathVariable Long questionId,
            Model model
    ) {
        QuestionDetailDTO question = adminQuestionBoardService.getQuestionDetail(questionId);

        model.addAttribute("question", question);
        model.addAttribute("comments", adminQuestionCommentService.getComments(questionId));

        return "questionboard/admin/questionboard-detail";
    }

    @DeleteMapping("/{questionId}/questionboard-delete")
    @ResponseBody
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        adminQuestionBoardService.deleteQuestion(questionId);
        return ResponseEntity.ok("문제가 삭제되었습니다.");
    }
}