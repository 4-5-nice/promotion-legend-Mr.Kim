package com.wanted.legendkim.domain.users.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/fail")
    public ModelAndView loginFail(@RequestParam String message, ModelAndView mv) {
        mv.addObject("message", message);
        mv.setViewName("auth/fail");
        return mv;
    }
}
