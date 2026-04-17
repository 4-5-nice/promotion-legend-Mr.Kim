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

    @PostMapping("/{postId}/comments")
    public String writeComment(@PathVariable Long postId, @RequestParam String content, Principal principal) {
        String email = principal != null ? principal.getName() : null;

        freeCommentService.writeComment(postId, content, email);

        return "redirect:/freeboard/user/freeboard/" + postId;
    }
}
