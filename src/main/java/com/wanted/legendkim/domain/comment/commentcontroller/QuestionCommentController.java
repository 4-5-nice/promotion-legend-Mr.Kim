package com.wanted.legendkim.domain.comment.commentcontroller;

import com.wanted.legendkim.domain.comment.commentservice.QuestionCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questionboard/user/questionboard")
public class QuestionCommentController {

    private final QuestionCommentService questionCommentService;

    @PostMapping("/{questionId}/comments")
    @ResponseBody
    public ResponseEntity<String> writeComment(@PathVariable Long questionId, // 어떤 게시글에
                                               @RequestParam String content, // 어떤 내용을
                                               Principal principal // 누가
    ) {
        String email = principal.getName(); // 작성자 이메일 가져오기

        questionCommentService.writeComment(questionId, content, email); // 댓글 쓰는 기능

        return ResponseEntity.ok("댓글이 등록되었습니다."); // 댓글이 저장되면 200을 전달하면서 메세지 반환
    }

    @PostMapping("/comments/{commentId}/edit")
    public String editComment(@PathVariable Long commentId, @RequestParam String content, Principal principal
    ) {
        String email = principal.getName();// 작성자 email 가져오기

        Long postId = questionCommentService.editComment(commentId, content, email); // 댓글 수정하기

        return "redirect:/questionboard/user/questionboard/" + postId; // 다시 게시글 상세 페이지로 이동
    }

    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, Principal principal
    ) {
        String email = principal.getName();// 작성자 email 가져오기

        Long postId = questionCommentService.deleteComment(commentId, email); // 댓글 삭제

        return "redirect:/questionboard/user/questionboard/" + postId; // 다시 게시글 상세 페이지로 이동
    }
}
