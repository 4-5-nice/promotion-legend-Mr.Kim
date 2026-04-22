package com.wanted.legendkim.global.config;

import com.wanted.legendkim.domain.users.auth.model.dto.AuthDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PaymentConfigInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return true;
        }

        // 로그인한 사용자의 Principal 객체 가져오기
        //Principal은 로그인한 사용자의 정보를 담는 객체
        Object principal = authentication.getPrincipal();
        boolean isPaid = false;

        // AuthDetails 객체로 캐스팅하여 loginUser 안의 isPaid 값 확인
        if (principal instanceof AuthDetails) {
            AuthDetails authDetails = (AuthDetails) principal;
            isPaid = authDetails.getLoginUser().getIsPaid();
        }

        if (!isPaid) {
            response.sendRedirect("/payment/info");
            // 컨트롤러로 요청이 넘어가지 않도록 false 반환
            return false;
        }
        return true;
    }
}