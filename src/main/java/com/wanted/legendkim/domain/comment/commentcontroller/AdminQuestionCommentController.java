package com.wanted.legendkim.domain.comment.commentcontroller;

import com.wanted.legendkim.domain.comment.commentservice.AdminQuestionCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questionboard/admin/questionboard/{questionId}/comments")
public class AdminQuestionCommentController {

    private final AdminQuestionCommentService adminQuestionCommentService;

    // 댓글 삭제하기
    @DeleteMapping("/{commentId}")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        adminQuestionCommentService.deleteComment(commentId); // 댓글 삭제하기
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
