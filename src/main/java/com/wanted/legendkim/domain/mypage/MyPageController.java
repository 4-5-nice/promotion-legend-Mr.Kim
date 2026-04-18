package com.wanted.legendkim.domain.mypage;

import com.wanted.legendkim.domain.mypage.entity.Attendance;
import com.wanted.legendkim.domain.mypage.entity.Payments;
import com.wanted.legendkim.domain.mypage.entity.Users;
import com.wanted.legendkim.domain.mypage.repository.PaymentsRepository;
import com.wanted.legendkim.domain.mypage.service.AttendanceService;
import com.wanted.legendkim.domain.mypage.service.EnrollmentsService;
import com.wanted.legendkim.domain.mypage.service.QuestionSubmissionsService;
import com.wanted.legendkim.domain.mypage.service.UsersService;
import com.wanted.legendkim.domain.mypage.DTO.UsersDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //나의 정보 페이지
    @GetMapping("/myPage/info")
    public String myInfoPage(Model model, Principal principal) {
        // 1. 현재 로그인한 사용자의 아이디(이메일 등) 가져오기
        String loginId = principal.getName();

        // 2. DB에서 사용자 상세 정보 조회 (Service/Repository 이용)
        UsersDTO user = userService.findByEmail(loginId);

        // 3. HTML로 데이터 전달
        model.addAttribute("user", user);
        model.addAttribute("inProgressList", enrollmentService.getInProgressEnrollments(loginId));
        model.addAttribute("completedList", enrollmentService.getCompletedEnrollments(loginId));

        return "mypage/myInfo"; // html 파일명
    }

    //신청한 강의 조회 페이지
    @GetMapping("/myPage/courses")
    public String myCoursesPage(Model model, Principal principal) {
        String loginId = principal.getName();

        UsersDTO user = userService.findByEmail(loginId);

        model.addAttribute("user", user);
        model.addAttribute("appliedCourse", enrollmentService.appliedCourse(loginId));

        return "mypage/courses";
    }

    //출결 조회 페이지
    @GetMapping("/myPage/attendance")
    public String myAttendancePage(Model model, Principal principal) {
        String loginId = principal.getName();

        // 1. 화면에 뿌릴 유저 정보(DTO)
        UsersDTO userDTO = userService.findByEmail(loginId);

        // 2. 서비스 호출 (로그인 ID 전달)
        List<Attendance> list = attendanceService.attendanceList(loginId);

        // 3. 통계 계산
        Map<String, Long> stats = attendanceService.getAttendanceInfo(list);

        model.addAttribute("user", userDTO);
        model.addAttribute("attendanceList", list);
        model.addAttribute("stats", stats);
        model.addAttribute("today", new java.util.Date());

        return "mypage/attendance";
    }

    //내가 푼 문제 조회 페이지
    @GetMapping("/myPage/quizhistory")
    public String quizHistory(Model model, Principal principal) {
        String loginId = principal.getName();

        // 유저 기본 정보
        UsersDTO userDTO = userService.findByEmail(loginId);

        // 퀴즈 통계 및 기록 (추가된 부분)
        Map<String, Object> quizData = questionSubmissionService.getQuizInfo(loginId);

        model.addAttribute("user", userDTO);
        model.addAttribute("quizHistory", quizData.get("history"));
        model.addAttribute("count", quizData); // totalSolved, totalPoints가 들어있음

        return "mypage/quizhistory";
    }

    //회원 탈퇴 페이지(정보 가져오기만)
    @GetMapping("/myPage/endjourney")
    public String endjourney(Model model, Principal principal) {
        String loginId = principal.getName();

        UsersDTO user = userService.findByEmail(loginId);

        model.addAttribute("user", user);

        return "mypage/endjourney";
    }

    //회원 탈퇴 버튼 누르면 관련 정보 삭제(아직 미구현)
    @PostMapping("/myPage/deleteuser")
    public String deleteUser() {
        return "auth/login";
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
            UsersDTO targetUser = userService.findByTargetUserId(targetUserId);
            model.addAttribute("targetUser", targetUser);

            // 수강생 출결 리스트 (달력용) - ID로 조회하는 메서드 새로 호출
            List<Attendance> list = attendanceService.attendanceListById(targetUserId);
            model.addAttribute("attendanceList", list);

            // 통계 계산
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
}
