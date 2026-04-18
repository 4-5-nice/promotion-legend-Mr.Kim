package com.wanted.legendkim.domain.questionboard.controller;

import com.wanted.legendkim.domain.comment.commentservice.QuestionCommentService;
import com.wanted.legendkim.domain.questionboard.dto.QuestionBoardDTO;
import com.wanted.legendkim.domain.questionboard.dto.QuestionDetailDTO;
import com.wanted.legendkim.domain.questionboard.dto.QuestionSolveResponseDTO;
import com.wanted.legendkim.domain.questionboard.dto.SectionDTO;
import com.wanted.legendkim.domain.questionboard.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questionboard/user/questionboard")
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;
    private final QuestionCommentService questionCommentService;

    // 문제 게시판 페이지 불러오기
    @GetMapping
    public String questionBoardPage(Model model, Principal principal) {
        String email = principal.getName();
        // 사용자 email 가져오기

        String myRank = questionBoardService.getMyRank(email); // email로 사용자의 직급 조회
        model.addAttribute("myRank", myRank); // model에 직급 저장

        return "questionboard/user/questionboard"; // model 객체 반환
    }

    // 문제 목록 불러오기
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<QuestionBoardDTO>> getQuestionList(@RequestParam String rank,
                                                                  Principal principal
    ) {
        String email = principal.getName();
        // 사용자 email 가져오기

        // 사용자의 직급과 이메일을 이용해서 가져올 문제 목록을 조회
        List<QuestionBoardDTO> questionList = questionBoardService.getQuestionList(rank, email);

        return ResponseEntity.ok(questionList); // 조회된 목록 반환
        // ResponseEntity => 응답 전체를 직접 제어할 수 있는 객체
        // ok => HTTP 상태코드 200을 의미
        // questionList => body에 들어갈 데이터
    }

    // 문제 출제 페이지 - course 목록도 함께 전달
    @GetMapping("/write")
    public String questionWritePage(Model model, Principal principal) {
        String email = principal.getName(); // 사용자 email 가져오기

        questionBoardService.validateWriteAccess(email); // 문제를 낼 수 있는지 검증
        model.addAttribute("courses", questionBoardService.getCourses());
        // 강좌의 정보를 model에 넘겨주기

        return "questionboard/user/questionboard-write"; // model 값을 반환
    }

    // course 선택시 section 목록 JSON 전달
    @GetMapping("/sections")
    @ResponseBody
    public ResponseEntity<List<SectionDTO>> getSectionsByCourse(@RequestParam Long courseId) {
        List<SectionDTO> sections = questionBoardService.getSectionsByCourse(courseId);
        // course 아이디를 이용해서 section 조회하기
        return ResponseEntity.ok(sections); // section들 반환
    }

    // 문제 출제하기
    @PostMapping("/submit")
    public String submitQuestion(@RequestParam String title, @RequestParam String option1,
                                 @RequestParam String option2, @RequestParam String option3,
                                 @RequestParam String option4, @RequestParam String option5,
                                 @RequestParam Integer answer, @RequestParam Long courseId,
                                 @RequestParam Long sectionId, Principal principal
    ) {
        String email = principal.getName(); // 사용자 email 가져오기

        questionBoardService.writeQuestion(title, option1, option2, option3, option4, option5,
                answer, courseId, sectionId, email
        ); // 문제 만들기

        return "redirect:/questionboard/user/questionboard"; // 문제 등록 후 문제 게시판으로 이동
    }

    // 문제 상세 조회
    @GetMapping("/{questionId}")
    public String questionDetailPage(@PathVariable Long questionId, Model model, Principal principal,
                                     RedirectAttributes redirectAttributes
                                  // RedirectAttributes 는 alert 메세지를 띄우기 위함
    ) {
        String email = principal.getName(); // 사용자 이메일 가져오기

        try {
            QuestionDetailDTO question = questionBoardService.getQuestionDetail(questionId, email);
                                                    // 문제 상세 정보를 가져오기

            model.addAttribute("question", question); // question에 담긴 값들을 model에 저장

            // 내가 풀었는지 안 풀었는지 확인
            if (question.isSolved()) { // 풀었으면 댓글 정보도 model에 추가
                model.addAttribute("comments", questionCommentService.getComments(questionId, email));
            } else { // 안 풀었으면 빈 리스트 담기
                model.addAttribute("comments", List.of());
            }

            return "questionboard/user/questionboard-detail"; // model 객체를 전송
        } catch (IllegalArgumentException e) { // 상세 조회에 문제가 생겼을 경우에만 실행
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
            //               에러메세지를 다음 페이지에서 보여주기 위해 잠시 저장
            return "redirect:/questionboard/user/questionboard"; // 그리고 문제 게시판으로 다시 이동
        }
    }

    // 정답 제출
    @PostMapping("/{questionId}/solve")
    @ResponseBody
    public ResponseEntity<QuestionSolveResponseDTO> solveQuestion(@PathVariable Long questionId,
                                                                  @RequestParam Integer selectedAnswer,
                                                                  Principal principal) {
        String email = principal.getName(); // 로그인 한 사용자 이메일 가져오기

        QuestionSolveResponseDTO result = questionBoardService.solveQuestion(questionId, selectedAnswer, email);
                                                  // 문제를 푸는 기능

        return ResponseEntity.ok(result);
        // HTTP에 응답을 보내는 코드
        // 응답 전체에.200으로 응담.응답body에 들어갈 데이터
    }
}
