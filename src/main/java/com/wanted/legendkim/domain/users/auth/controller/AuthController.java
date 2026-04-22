package com.wanted.legendkim.domain.users.auth.controller;

import com.wanted.legendkim.domain.users.user.model.dto.PasswordResetDTO;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            // 로그인 실패시 실패 횟수와 메시지 전달
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "failCount", required = false) Integer failCount,
            Model model
    ) {
        // model에 담아서 타임리프가 쓸 수 있도록 반환
        model.addAttribute("message", message);
        model.addAttribute("failCount", failCount);

        return "user/login";
    }

    // 비밀번호 재설정 페이지 띄우기
    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "user/reset-password";
    }

    // 비밀번호 재설정 처리
    @PostMapping("/reset-password")
    public String processResetPassword(@ModelAttribute PasswordResetDTO dto, Model model) {
        //1차 검증 ModelAttribute로 입력받은 정보를 Dto 객체를 통해 포장함
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("status", "error");
            return "user/reset-password";
        }

        boolean isSuccess = memberService.resetPassword(dto);

        if (isSuccess) { //성공 반환시
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다. 새 비밀번호로 로그인해주세요.");
            model.addAttribute("status", "success");
            return "user/login";
        } else { //실패 반환 시
            model.addAttribute("message", "이메일이 존재하지 않거나 보안 답변이 틀렸습니다.");
            model.addAttribute("status", "error");
            return "user/reset-password";
        }
    }


}