package com.wanted.legendkim.domain.users.auth.handler;

import com.wanted.legendkim.domain.users.user.model.dao.LoginLogRepository;
import com.wanted.legendkim.domain.users.user.model.dto.LoginUserDTO;
import com.wanted.legendkim.domain.users.user.model.entity.LoginHistory;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private final MemberService memberService;
    private final LoginLogRepository loginLogRepository;

    public AuthFailHandler(MemberService memberService, LoginLogRepository loginLogRepository) {
        this.memberService = memberService;
        this.loginLogRepository = loginLogRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;
        String email = request.getParameter("email");

        // 💡 1. 여기서 강현 님이 작성하신 분기별 에러 메시지가 세팅됩니다.
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            errorMessage = "이메일이 존재하지 않거나 비밀번호가 일치하지 않습니다.";
            if (email != null && !email.isEmpty()) {
                memberService.incrementLoginFailCount(email);
                errorMessage += " (5회 실패 시 계정 잠금)";
            }
        } else if (exception instanceof LockedException) {
            errorMessage = "계정이 잠겨있습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "서버에서 오류가 발생되었습니다.";
        } else {
            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다.";
        }

        LoginUserDTO loginUser = memberService.findByEmail(email);
        int failCount = 0; // 화면에 넘겨줄 실패 횟수

        if (loginUser != null) {
            failCount = loginUser.getLoginFailCount(); // 최신 실패 횟수 가져오기

            loginLogRepository.save(
                    new LoginHistory(
                            loginUser.getUserId(),
                            false,
                            errorMessage, // DB에도 이 분기별 에러 메시지가 그대로 저장됩니다!
                            LocalDateTime.now()
                    )
            );
        }

        // 💡 2. 세팅된 분기별 에러 메시지와 실패 횟수를 /auth/login 화면으로 전달합니다.
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        String redirectUrl = "/auth/login?message=" + errorMessage + "&failCount=" + failCount;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}