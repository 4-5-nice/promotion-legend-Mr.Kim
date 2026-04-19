package com.wanted.legendkim.domain.freeboard.boardcontroller;

import com.wanted.legendkim.domain.comment.commentservice.AdminFreeCommentService;
import com.wanted.legendkim.domain.comment.dto.FreeCommentDTO;
import com.wanted.legendkim.domain.freeboard.boardservice.AdminFreeBoardService;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDTO;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freeboard/admin/freeboard")
public class AdminFreeBoardController {

    private final AdminFreeBoardService adminFreeBoardService;
    private final AdminFreeCommentService adminFreeCommentService;

    @GetMapping
    public String adminFreeBoardPage() {
        return "freeboard/admin/freeboard";
    }

    @ResponseBody
    @GetMapping("/posts")
    public List<FreeBoardDTO> getPosts() {
        return adminFreeBoardService.getAdminPosts();
    }

    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, Model model) {
        FreeBoardDetailDTO post = adminFreeBoardService.getAdminPostDetail(postId);
        List<FreeCommentDTO> comments = adminFreeCommentService.getComments(postId);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);

        return "freeboard/admin/freeboard-detail";
    }

    @PostMapping("/{postId}/freeboard-delete")
    public String deletePost(@PathVariable Long postId) {
        adminFreeBoardService.deletePostByAdmin(postId);
        return "redirect:/freeboard/admin/freeboard";
    }

}
