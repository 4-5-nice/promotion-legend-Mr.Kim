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

    // 관리자용 자유게시판 페이지 불러오기
    @GetMapping
    public String adminFreeBoardPage() {
        return "freeboard/admin/freeboard";
    }

    // 게시글 목록 불러오기
    @ResponseBody
    @GetMapping("/posts")
    public List<FreeBoardDTO> getPosts() {
        return adminFreeBoardService.getAdminPosts();
    }

    // 게시글 상세조회 페이지 이동
    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, Model model) {
        //@PathVariable => url 경로에 있는 값을 메서드 파라미터로 받아오기.

        FreeBoardDetailDTO post = adminFreeBoardService.getAdminPostDetail(postId);
        // 게시글 정보 찾기
        List<FreeCommentDTO> comments = adminFreeCommentService.getComments(postId);
        // 댓글 정보 찾기

        model.addAttribute("post", post); // 게시글 정보를 model에 담기
        model.addAttribute("comments", comments); // 댓글정보를 model에 담기

        return "freeboard/admin/freeboard-detail"; // 그것들을 상세조회 페이지로 전달
    }

    @PostMapping("/{postId}/freeboard-delete")
    public String deletePost(@PathVariable Long postId) {
        adminFreeBoardService.deletePostByAdmin(postId); // 게시글 삭제
        return "redirect:/freeboard/admin/freeboard"; // 다시 자유게시판으로 이동
    }

}
