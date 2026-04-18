package com.wanted.legendkim.domain.users.auth.handler;

import com.wanted.legendkim.domain.users.auth.model.dto.AuthDetails;
import com.wanted.legendkim.domain.users.user.model.dao.LoginLogRepository;
import com.wanted.legendkim.domain.users.user.model.entity.LoginHistory;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;
    private final LoginLogRepository loginLogRepository;

    public AuthSuccessHandler(MemberService memberService, LoginLogRepository loginLogRepository) {
        this.memberService = memberService;
        this.loginLogRepository = loginLogRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        AuthDetails userDetails = (AuthDetails) authentication.getPrincipal();

        String email = userDetails.getEmail();
        Long userId = userDetails.getUserId();

        memberService.resetLoginFailCount(email);

        loginLogRepository.save(
                new LoginHistory(
                userId,
                true,
                null,
                LocalDateTime.now())
        );

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().contains("ADMIN"));

        if (isAdmin) {
            // 관리자일 경우 관리자 전용 대시보드로 이동
            getRedirectStrategy().sendRedirect(request, response, "/admin/main"); // 👈 관리자 URL
        } else {
            // 일반 유저일 경우 메인 페이지로 이동
            getRedirectStrategy().sendRedirect(request, response, "/"); // 👈 일반 유저 URL
        }
    }
}