package com.wanted.legendkim.domain.users.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 💡 Model 임포트 필수
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

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

}