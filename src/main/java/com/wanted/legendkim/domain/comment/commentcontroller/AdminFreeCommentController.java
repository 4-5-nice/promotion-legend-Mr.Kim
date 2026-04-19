package com.wanted.legendkim.domain.comment.commentcontroller;

import com.wanted.legendkim.domain.comment.commentservice.AdminFreeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freeboard/admin/freeboard")
public class AdminFreeCommentController {

    private final AdminFreeCommentService adminFreeCommentService;

    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId) {
        Long postId = adminFreeCommentService.deleteCommentByAdmin(commentId);

        return "redirect:/freeboard/admin/freeboard/" + postId;
    }
}