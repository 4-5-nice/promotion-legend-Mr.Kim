package com.wanted.legendkim.domain.users.user.controller;

import com.wanted.legendkim.domain.users.user.model.dao.LoginLogRepository;
import com.wanted.legendkim.domain.users.user.model.entity.LoginHistory;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;
    private final LoginLogRepository loginLogRepository;

    public AdminController(MemberService memberService, LoginLogRepository loginLogRepository) {
        this.memberService = memberService;
        this.loginLogRepository = loginLogRepository;
    }

    /**
     * 관리자 메인 대시보드
     */
    @GetMapping("/main")
    public String adminHome(Model model) {
        // 사이드바 알림 배지를 위해 잠긴 계정 수 조회
        model.addAttribute("lockedCount", memberService.getLockedUserCount());

        return "user/admin";
    }

    /**
     * 잠긴 계정 목록 조회 및 검색
     */
    @GetMapping("/locked-users")
    public String lockedUsers(
            //검색 없어도 조회 가능
            @RequestParam(value = "keyword", required = false) String keyword,
            //페이징 기능
            @PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        // 검색어(이름) 유무에 따른 페이징 결과 조회 - (누가, 몇페이지에 있는지)
        Page<User> userPage = memberService.getLockedUsers(keyword, pageable);
        model.addAttribute("users", userPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("lockedCount", memberService.getLockedUserCount());

        return "user/locked-list";
    }

    /**
     * 계정 잠금 해제 처리
     */
    @PostMapping("/unlock-user")
    public String unlockUser(@RequestParam("userId") Long userId) {
        memberService.unlockUser(userId);
        // 처리 후 다시 잠긴 계정 목록으로 리다이렉트
        return "redirect:/admin/locked-users";
    }

    /**
     * 상세 로그 모달창 조회를 위한 API (JSON 반환)
     */
    @ResponseBody
    @GetMapping("/api/user-logs/{userId}")
    public List<LoginHistory> getUserLogs(@PathVariable("userId") Long userId) {
        // LoginHistory 엔티티의 createdAt 필드명에 맞춰 Repository 메서드 호출
        return loginLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/attendance")
    public String attendanceList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        // 전체 수강생 중 검색 결과 조회
        Page<User> userPage = memberService.getAllUsers(keyword, pageable);

        model.addAttribute("users", userPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("lockedCount", memberService.getLockedUserCount()); // 사이드바 배지 유지

        return "user/attendance-list";
    }
}