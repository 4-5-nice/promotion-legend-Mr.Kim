package com.wanted.legendkim.domain.freeboard.boardcontroller;

import com.wanted.legendkim.domain.freeboard.boardservice.FreeBoardService;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDTO;
import com.wanted.legendkim.domain.comment.commentservice.FreeCommentService;
import com.wanted.legendkim.domain.comment.dto.FreeCommentDTO;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freeboard/user/freeboard")
public class FreeBoardController {

    private final FreeBoardService freeBoardService;
    private final FreeCommentService freeCommentService;

    @GetMapping
    public String freeBoardPage() {
        return "/freeboard/user/freeboard";
    }

    // 게시글 목록 데이터 반환
    @ResponseBody
    @GetMapping("/posts")
    public List<FreeBoardDTO> getPosts(@RequestParam(defaultValue = "all") String filter,
                                       Principal principal
    ) {
        String email = principal != null ? principal.getName() : null;

        return freeBoardService.getPosts(filter, email);
    }

    // 상세 조회
    @GetMapping("/{postId}")
    public String detail(
            @PathVariable Long postId,
            Principal principal,
            Model model
    ) {
        String email = principal != null ? principal.getName() : null;

        FreeBoardDetailDTO post = freeBoardService.getPostDetail(postId, email);
        List<FreeCommentDTO> comments = freeCommentService.getComments(postId);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);

        return "freeboard/user/freeboard-detail";
    }

    @GetMapping("/freeboard-write")
    public String writePage() {
        return "freeboard/user/freeboard-write";
    }

    // 글 등록
    @PostMapping("/freeboard-write")
    public String writePost(
            @RequestParam String title,
            @RequestParam String content,
            Principal principal
    ) {
        String email = principal != null ? principal.getName() : null;

        freeBoardService.writePost(title, content, email);

        return "redirect:/freeboard/user/freeboard";
    }


}
