package com.wanted.legendkim.domain.comment.commentcontroller;

import com.wanted.legendkim.domain.comment.commentservice.FreeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freeboard/user/freeboard")
public class FreeCommentController {

    private final FreeCommentService freeCommentService;

    // 댓글 등록
    @PostMapping("/{postId}/comments")
    public String writeComment(@PathVariable Long postId, @RequestParam String content, Principal principal)
    {                           // 어느 게시글에 등록할건지,            어떤 내용을 등록할건지,   누가 등록할건지
        String email = principal.getName();
        // 작성자 email 가져오기

        freeCommentService.writeComment(postId, content, email); // 댓글 쓰는 기능

        return "redirect:/freeboard/user/freeboard/" + postId; // 다시 게시글 상세 페이지로 이동
    }

    // 댓글 수정
    @PostMapping("/comments/{commentId}/edit")
    public String editComment(@PathVariable Long commentId, @RequestParam String content, Principal principal
    ) {
        String email = principal.getName();// 작성자 email 가져오기

        Long postId = freeCommentService.editComment(commentId, content, email); // 댓글 수정하기

        return "redirect:/freeboard/user/freeboard/" + postId; // 다시 게시글 상세 페이지로 이동
    }

    // 댓글 삭제
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, Principal principal
    ) {
        String email = principal.getName();// 작성자 email 가져오기

        Long postId = freeCommentService.deleteComment(commentId, email); // 댓글 삭제

        return "redirect:/freeboard/user/freeboard/" + postId; // 다시 게시글 상세 페이지로 이동
    }
}
