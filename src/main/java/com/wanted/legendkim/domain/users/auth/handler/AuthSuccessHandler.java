package com.wanted.legendkim.domain.users.auth.handler;

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
        String email = authentication.getName();

        // 실패 횟수 초기화
        memberService.resetLoginFailCount(email);

        // 성공 로그 저장 (LoginHistory 엔티티 생성자에 맞게 수정 필요)
        loginLogRepository.save(
                new LoginHistory(
                        email,
                        LocalDateTime.now(),
                        true,
                        request.getRemoteAddr()
                )
        );

        super.onAuthenticationSuccess(request, response, authentication);
    }
}