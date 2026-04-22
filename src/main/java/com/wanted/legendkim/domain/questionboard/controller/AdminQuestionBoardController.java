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

    // 관리자용 문제게시판 불러오기
    @GetMapping
    public String adminQuestionBoardPage(Model model) {
        model.addAttribute("pageType", "admin");
        return "questionboard/admin/questionboard";
    }

    // 문제게시판에서 문제 목록 불러오기
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<QuestionBoardDTO>> getQuestionList(@RequestParam String rank) {
        List<QuestionBoardDTO> questionList = adminQuestionBoardService.getQuestionList(rank);
        // 직급에 따라 다르게 문제 목록 불러오기
        return ResponseEntity.ok(questionList); // json 값 넘겨주기
    }

    // 문제 상세조회 페이지 불러오기
    @GetMapping("/{questionId}")
    public String adminQuestionDetailPage(@PathVariable Long questionId, Model model) {
        QuestionDetailDTO question = adminQuestionBoardService.getQuestionDetail(questionId);
        // 문제 아이디를 이용하여 문제 상세 정보 찾기

        model.addAttribute("pageType", "admin");
        model.addAttribute("question", question); // 문제 상세 정보를 model에 담기
        model.addAttribute("comments", adminQuestionCommentService.getComments(questionId));
        // model 에 담을 댓글을 문제 아이디를 이용하여 조회

        return "questionboard/admin/questionboard-detail"; // model 정보를 상세조회 페이지로 반환
    }

    // 문제삭제하기
    @PostMapping("/{questionId}/questionboard-delete")
    @ResponseBody
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        adminQuestionBoardService.deleteQuestion(questionId); // 문제 아이디로 삭제
        return ResponseEntity.ok().build(); // 알림 메세지 전달
    }
}