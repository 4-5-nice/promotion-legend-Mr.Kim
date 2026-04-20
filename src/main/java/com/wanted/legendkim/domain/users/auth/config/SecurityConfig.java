package com.wanted.legendkim.domain.users.auth.config;

import com.wanted.legendkim.domain.users.auth.handler.AuthFailHandler;
import com.wanted.legendkim.domain.users.auth.handler.AuthSuccessHandler;
import com.wanted.legendkim.domain.users.auth.model.service.AuthService;
import com.wanted.legendkim.domain.users.user.model.dao.LoginLogRepository;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    // 정적 리소스(CSS, JS, 이미지 등)는 보안 필터를 거치지 않도록 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // 성공 핸들러 빈 등록
    @Bean
    public AuthSuccessHandler authSuccessHandler(MemberService memberService, LoginLogRepository loginLogRepository) {
        return new AuthSuccessHandler(memberService, loginLogRepository);
    }

    // 실패 핸들러 빈 등록
    @Bean
    public AuthFailHandler authFailHandler(MemberService memberService, LoginLogRepository loginLogRepository) {
        return new AuthFailHandler(memberService, loginLogRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthSuccessHandler authSuccessHandler,
                                           AuthFailHandler authFailHandler) throws Exception {
        http
                .userDetailsService(authService)
                .authorizeHttpRequests(auth -> auth
                        // 💡 1. 누구나 접근 가능한 경로 (가장 구체적인 것부터 설정)
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .requestMatchers("/auth/**", "/user/signup").permitAll()

                        // 💡 2. 권한별 접근 제한 (중요!)
                        // /admin으로 시작하는 경로는 ADMIN 권한이 있어야만 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // /user로 시작하는 경로는 USER 권한이 있어야만 접근 가능 (ADMIN도 USER 권한을 포함하므로 접근 가능)
                        .requestMatchers("/user/**").hasRole("USER")

                        // 💡 3. 마무리 설정: 그 외 모든 요청은 인증(로그인)이 필요함
                        // (반드시 맨 마지막에 위치해야 하며, anyRequest() 뒤에 다른 설정을 추가하면 에러가 납니다)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(conf -> conf
                        // 인증되지 않은 사용자가 보호된 리소스에 접근할 경우 메인으로 보내며 에러 메시지 전달
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/?error=login_required"))
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")            // 로그인 페이지 주소
                        .loginProcessingUrl("/login")        // 로그인 처리 주소 (HTML Form의 action과 일치)
                        .usernameParameter("email")          // 로그인 ID로 사용할 파라미터명
                        .passwordParameter("password")       // 로그인 비밀번호로 사용할 파라미터명
                        .successHandler(authSuccessHandler)  // 성공 시 커스텀 핸들러 실행
                        .failureHandler(authFailHandler)     // 실패 시 커스텀 핸들러 실행
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")                // 로그아웃 처리 주소
                        .logoutSuccessUrl("/")               // 로그아웃 성공 시 이동할 주소
                        .invalidateHttpSession(true)         // 세션 무효화
                        .deleteCookies("JSESSIONID")         // 쿠키 삭제
                        .permitAll()
                );

        return http.build();
    }
}