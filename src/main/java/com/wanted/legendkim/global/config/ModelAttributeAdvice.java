package com.wanted.legendkim.global.config;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice // @ControllerAdvice => 모든 컨트롤러에 공통으로 적용되는 보조 클래스
public class ModelAttributeAdvice {

    @ModelAttribute // @ModelAttribute => 컨트롤러의 메서드가 실행되기 전에, model에 공통 데이터를 넣는 메서드
    public void addCommonAttributes(Model model, Authentication authentication) {
                                               // 현재 로그인한 사용자의 인증 정보 담음
        boolean isAdmin = false;

        if (authentication != null) { // 로그인 했다면
            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        } // 현재 사용자의 권한 목록을 가져와서 권한 목록을 하나씩 stream으로 바꿔서 ADMIN 이랑 같은게 있는가?

        model.addAttribute("isAdmin", isAdmin); // 있으면 model에 담기

    }
}