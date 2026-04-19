package com.wanted.legendkim.domain.users.auth.controller;

import com.wanted.legendkim.domain.users.user.model.dto.PasswordResetDTO;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 💡 Model 임포트 필수
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String login(
            // 💡 URL 주소에 달려오는 파라미터를 받습니다.
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "failCount", required = false) Integer failCount,
            Model model
    ) {
        // 💡 받은 파라미터를 HTML(Thymeleaf)에서 쓸 수 있게 Model에 담아줍니다.
        model.addAttribute("message", message);
        model.addAttribute("failCount", failCount);

        return "user/login";
    }

    // 💡 비밀번호 재설정 페이지 띄우기
    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "user/reset-password";
    }

    // 💡 비밀번호 재설정 처리
    @PostMapping("/reset-password")
    public String processResetPassword(@ModelAttribute PasswordResetDTO dto, Model model) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("status", "error"); // 에러 상태 추가
            return "user/reset-password";
        }

        boolean isSuccess = memberService.resetPassword(dto);

        if (isSuccess) {
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다. 새 비밀번호로 로그인해주세요.");
            model.addAttribute("status", "success"); // ✨ 성공 상태 추가
            return "user/login";
        } else {
            model.addAttribute("message", "이메일이 존재하지 않거나 보안 답변이 틀렸습니다.");
            model.addAttribute("status", "error"); // 에러 상태 추가
            return "user/reset-password";
        }
    }


}