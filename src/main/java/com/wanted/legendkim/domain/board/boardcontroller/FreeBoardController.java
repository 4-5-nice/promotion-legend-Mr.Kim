package com.wanted.legendkim.domain.board.boardcontroller;

import com.wanted.legendkim.domain.board.boardservice.FreeBoardService;
import com.wanted.legendkim.domain.board.dto.FreeBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freeboard/user/freeboard")
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

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
    public String detail(@PathVariable Long postId,
                         Principal principal,
                         Model model) {

        String email = principal.getName();

        FreeBoardDTO post = freeBoardService.getPostDetail(postId, email);

        model.addAttribute("post", post);

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
