package com.wanted.legendkim.domain.mypage;

import com.wanted.legendkim.domain.mypage.entity.MPAttendance;
import com.wanted.legendkim.domain.mypage.service.*;
import com.wanted.legendkim.domain.mypage.DTO.UsersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final UsersService userService;
    private final EnrollmentsService enrollmentService;
    private final AttendanceService attendanceService;
    private final QuestionSubmissionsService questionSubmissionService;
    private final CommentsService commentService;
    private final CoursesService courseService;
    private final FreeBoardsService freeboardService;
    private final LoginHistoryService loginHistoryService;
    private final PaymentsService paymentService;
    private final QuestionsService questionService;
    private final SectionsService sectionService;
    private final VacationHistoryService vacationHistoryService;


    //나의 정보 페이지
    @GetMapping("/myPage/info")
    public String myInfoPage(Model model, Principal principal) {
        // 1. 현재 로그인한 사용자의 아이디(이메일 등) 가져오기
        String loginId = principal.getName();

        // 2. DB에서 사용자 상세 정보 조회 (Service/Repository 이용)
        UsersDTO user = userService.findByEmail(loginId);

        // 3. HTML로 데이터 전달
        //사용자 정보(이름, 직급, 이메일, 연차 수, 포인트, 결제 내역(userService쪽에 추가됨))
        model.addAttribute("user", user);
        //수강 중인 강의 정보(강의명)
        model.addAttribute("inProgressList", enrollmentService.getInProgressEnrollments(loginId));
        //수강 완료 강의 정보(강의명, 수료일)
        model.addAttribute("completedList", enrollmentService.getCompletedEnrollments(loginId));

        return "mypage/myInfo"; // html 파일명
    }

    //신청한 강의 조회 페이지
    @GetMapping("/myPage/courses")
    public String myCoursesPage(Model model, Principal principal) {
        //로그인한 사용자 아이디
        String loginId = principal.getName();

        //이메일 기준으로 사용자 정보 가져오기
        UsersDTO user = userService.findByEmail(loginId);

        //사용자 정보(이름, 이메일, 직급, 포인트 등)
        model.addAttribute("user", user);
        //수강 정보(강의명, 교수명, 신청일, 수강상태)
        model.addAttribute("appliedCourse", enrollmentService.appliedCourse(loginId));

        return "mypage/courses";
    }

    //출결 조회 페이지
    @GetMapping("/myPage/attendance")
    public String myAttendancePage(Model model, Principal principal) {
        //로그인 사용자의 정보
        String loginId = principal.getName();

        // 1. 화면에 뿌릴 유저 정보(DTO)
        UsersDTO userDTO = userService.findByEmail(loginId);

        // 2. 서비스 호출 (로그인 ID 전달) //출결 정보 가져와서 리스트에 담기
        List<MPAttendance> list = attendanceService.attendanceList(loginId);

        // 3. 통계 계산 //list에 담긴 출결 정보를 계산을 위해 맵으로 담기
        Map<String, Long> stats = attendanceService.getAttendanceInfo(list);

        //사용자 정보
        model.addAttribute("user", userDTO);
        //출결 정보
        model.addAttribute("attendanceList", list);
        //계산할 출결 정보(총 출근일, 결석일, 결근일 등)
        model.addAttribute("stats", stats);
        //캘린더에 표시할 현재 날짜
        model.addAttribute("today", new java.util.Date());

        return "mypage/attendance";
    }

    //연차 사용하기 화면
    @GetMapping("/myPage/usePTO")
    public String usePTO(Model model, Principal principal){
        //로그인한 사용자
        String loginId = principal.getName();

        // 1. 화면에 뿌릴 유저 정보(DTO)
        UsersDTO userDTO = userService.findByEmail(loginId);

        // 2. 서비스 호출 (로그인 ID 전달) //출결 정보
        List<MPAttendance> list = attendanceService.attendanceList(loginId);

        // 3. 통계 계산 //계산할 출결 정보(사용하진 않음)
        Map<String, Long> stats = attendanceService.getAttendanceInfo(list);

        model.addAttribute("user", userDTO);
        model.addAttribute("attendanceList", list);
        model.addAttribute("stats", stats); //(사용하진 않음)
        model.addAttribute("today", new java.util.Date()); //캘린더에 표시할 오늘 날짜

        return "mypage/usepto";
    }

    //연차 사용
    @PostMapping("myPage/PTOApply")
    @ResponseBody
    public ResponseEntity<String> ptoApply(@RequestBody Map<String, Object> data, Principal principal) {
        // 1. 데이터가 비어있는지 체크 (안전장치) //받아온 정보가 없는지 확인
        // html에서 연차 사용할 날짜, 사유, 상세 사유를 data에 받아옴.
        if (data == null || data.isEmpty()) {
            return ResponseEntity.badRequest().body("신청 정보가 없습니다.");
        }

        // 2. 서비스로 데이터 넘겨서 처리 (결과를 boolean 등으로 받기)
        // 서비스 메서드에서 처리가 잘 되면 true, 아니면 false를 주게 짜면 좋습니다.
        //로그인 사용자와 html에서 받은 정보로 연차 계산
        boolean isSuccess = attendanceService.registerVacation(principal.getName(), data);

        // 3. 결과에 따른 처리
        if (isSuccess) {
            return ResponseEntity.ok("success");
        } else { //정보가 없거나 현재보다 이전 날짜 누르면
            return ResponseEntity.status(500).body("fail");
        }
    }

    //내가 푼 문제 조회 페이지
    @GetMapping("/myPage/quizhistory")
    public String quizHistory(Model model, Principal principal) {
        //로그인한 사용자
        String loginId = principal.getName();

        // 유저 기본 정보
        UsersDTO userDTO = userService.findByEmail(loginId);

        // 퀴즈 통계 및 기록 //사용자의 퀴즈 제출 정보를 맵에 담기
        Map<String, Object> quizData = questionSubmissionService.getQuizInfo(loginId);

        //사용자 정보
        model.addAttribute("user", userDTO);
        model.addAttribute("quizHistory", quizData.get("history"));
        model.addAttribute("count", quizData); // totalSolved, totalPoints가 들어있음

        return "mypage/quizhistory";
    }

    //회원 탈퇴 페이지(정보 가져오기만)
    @GetMapping("/myPage/endjourney")
    public String endjourney(Model model, Principal principal) {
        //로그인 사용자 정보
        String loginId = principal.getName();

        UsersDTO user = userService.findByEmail(loginId);

        //사용자 정보(이름, 이메일, 직급, 포인트 등)
        model.addAttribute("user", user);

        return "mypage/endjourney";
    }

    //회원 탈퇴 버튼 누르면 관련 정보 삭제
    @PostMapping("/myPage/deleteuser")
    public String deleteUser(Principal principal) {
        // 1. 누구인지 확인
        String loginId = principal.getName();

        // 2. 서비스야, 이 사람 데이터 싹 다 지워라 (순서대로 13개!)
        userService.deleteUserAllData(loginId);

        return "redirect:/login"; //또는 "/" 메인으로? & 추후 합친 후엔 auth/login으로.
    }

    //관리자용 출결 조회 페이지(정보 가져오기만)
    @GetMapping("/myPage/adminattendance/{userId}")
    public String modifyAttendance(Model model, Principal principal,
                                   @PathVariable(name="userId", required = false) Long targetUserId) {
        // 1. [사이드바] 로그인한 관리자 정보
        String loginId = principal.getName();
        UsersDTO admin = userService.findByEmail(loginId);
        model.addAttribute("admin", admin);

        // 2. [중앙 카드 & 출결 달력] 관리 대상 수강생 정보
        if (targetUserId != null) {
            // 수강생 기본 정보 (상단 카드용)
            //수정할 사용자의 정보(브라우저 /{userId} 아이디 기준)
            UsersDTO targetUser = userService.findByTargetUserId(targetUserId);
            model.addAttribute("targetUser", targetUser);

            // 수강생 출결 리스트 (달력용) - ID로 조회하는 메서드 새로 호출
            //수정할 사용자의 출결 정보
            List<MPAttendance> list = attendanceService.attendanceListById(targetUserId);
            model.addAttribute("attendanceList", list);

            // 통계 계산(사용하진 않음)
            Map<String, Long> stats = attendanceService.getAttendanceInfo(list);
            model.addAttribute("stats", stats);
        } else {
            // userId가 없을 경우를 대비해 빈 객체라도 넣어줘야 타임리프 에러가 안 납니다.
            model.addAttribute("targetUser", new UsersDTO());
            model.addAttribute("stats", new java.util.HashMap<String, Long>());
            model.addAttribute("attendanceList", new java.util.ArrayList<>());
        }

        model.addAttribute("today", new java.util.Date());

        return "mypage/adminattendance";
    }

    //관리자용 출결 수정하기
    @PostMapping("/myPage/attendanceupdate")
    @ResponseBody
    public ResponseEntity<String> updateAttendance(@RequestBody List<Map<String, Object>> updateList) {
        // 1. 데이터가 비어있는지 체크 (if-else)
        //html에서 수정한 내역들을 updateList에 담기
        if (updateList == null || updateList.isEmpty()) {
            return ResponseEntity.badRequest().body("수정할 데이터가 없습니다.");
        }

        // 2. 서비스로 데이터 넘기기
        boolean isSuccess = attendanceService.updateAttendanceStatus(updateList);

        // 3. 결과에 따른 처리
        if (isSuccess) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(500).body("fail");
        }
    }

    @GetMapping("/")
    public String index(Principal principal) {
        // 시큐리티가 정상 작동하면, 로그인이 안 된 유저는 이미 /login 페이지에 갇혀 있습니다.
        // 여기 도달했다는 건 무조건 Principal이 있다는 뜻입니다.
        return "redirect:/myPage/info";
    }
}
