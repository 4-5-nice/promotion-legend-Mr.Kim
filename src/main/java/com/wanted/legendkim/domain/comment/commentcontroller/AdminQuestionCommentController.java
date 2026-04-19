package com.wanted.legendkim.domain.comment.commentcontroller;

import com.wanted.legendkim.domain.comment.commentservice.AdminQuestionCommentService;
import com.wanted.legendkim.domain.comment.dto.QuestionCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questionboard/admin/questionboard/{questionId}/comments")
public class AdminQuestionCommentController {

    private final AdminQuestionCommentService adminQuestionCommentService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<QuestionCommentDTO>> getComments(@PathVariable Long questionId) {
        List<QuestionCommentDTO> comments = adminQuestionCommentService.getComments(questionId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    @ResponseBody
    public ResponseEntity<String> deleteComment(
            @PathVariable Long questionId,
            @PathVariable Long commentId
    ) {
        adminQuestionCommentService.deleteComment(questionId, commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
