package com.wanted.legendkim.global.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    //메인페이지(초기화면)
    @GetMapping("/")
    public String index() {
        return "main/index";
    }
}
