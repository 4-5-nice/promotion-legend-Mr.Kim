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

    // 자유게시판 페이지 불러오기
    @GetMapping
    public String freeBoardPage() {
        return "/freeboard/user/freeboard";
    }

    // 게시글 목록 데이터 반환
    @ResponseBody
    @GetMapping("/posts")
    public List<FreeBoardDTO> getPosts(@RequestParam(defaultValue = "all") String filter,
                                       // filter로 보여줄 게시글 구분할 것이다. all은 전체, mine은 자기글
                                       Principal principal // Principal 은 로그인 한 사용자 정보를 나타내는 객체
    ) {
        String email = principal.getName();
        // 우리는 로그인을 email 로 해서 식별값이 email이다. principal에 담긴 email을 꺼낸다.
        // 여기서 getName은 이름이 아니라 식별값 이름이라는 뜻. (우리의 경우에 email의 값)
        return freeBoardService.getPosts(filter, email);
    }

    // 상세 조회
    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, // url 경로의 변수값(게시글 아이디) 받아오기
                         Principal principal,Model model
    ) {
        String email = principal.getName();
        // principal에 저장된 email 꺼내오기

        // 게시글 상세정보 가져오기
        FreeBoardDetailDTO post = freeBoardService.getPostDetail(postId, email);
        // 댓글들 불러오기
        List<FreeCommentDTO> comments = freeCommentService.getComments(postId, email);

        model.addAttribute("post", post); // model에 게시글 정보 담아주기
        model.addAttribute("comments", comments); // model에 댓글 정보 담아주기

        return "freeboard/user/freeboard-detail"; // model 내용을 url 페이지에 보내기
    }

    // 글 작성 페이지 불러오기
    @GetMapping("/freeboard-write")
    public String writePage(Model model) {
        model.addAttribute("isEdit", false);
        // 현재 등록모드(false)인지 수정모드(true)인지 체크
        return "freeboard/user/freeboard-write";
    }

    // 글 등록
    @PostMapping("/freeboard-write")
    public String writePost(@RequestParam String title, @RequestParam String content, Principal principal) {
        String email = principal.getName();
        // 로그인 한 사용자 email 가져오기

        freeBoardService.writePost(title, content, email);

        return "redirect:/freeboard/user/freeboard"; // 다시 자유게시판 페이지로 이동
    }

    // 글 수정 페이지 불러오기
    @GetMapping("/{postId}/freeboard-write")
    public String editPage(@PathVariable Long postId, Principal principal, Model model) {
        String email = principal.getName(); // 로그인 한 사용자 email 가져오기

        FreeBoardDetailDTO post = freeBoardService.getEditPost(postId, email); // 게시글 수정하기

        model.addAttribute("post", post); // 수정한 게시글 정보 담기
        model.addAttribute("isEdit", true);
        // 현재 등록모드(false)인지 수정모드(true)인지 체크

        return "freeboard/user/freeboard-write"; // 수정 페이지로 이동
    }

    // 글 수정
    @PostMapping("/{postId}/freeboard-edit")
    public String editPost(@PathVariable Long postId, @RequestParam String title,
                           @RequestParam String content, Principal principal) {
        String email = principal.getName(); // 사용자 email 가져오기

        freeBoardService.editPost(postId, title, content, email); // 게시글 수정하기

        return "redirect:/freeboard/user/freeboard/" + postId; // 다시 상세조회 페이지로 이동
    }

    // 글 삭제
    @PostMapping("/{postId}/freeboard-delete")
    public String deletePost(@PathVariable Long postId, Principal principal) {
        String email = principal.getName(); // 사용자 email 가져오기

        freeBoardService.deletePost(postId, email); // 게시글 삭제하기

        return "redirect:/freeboard/user/freeboard"; // 자유게시판 페이지로 이동
    }


}
