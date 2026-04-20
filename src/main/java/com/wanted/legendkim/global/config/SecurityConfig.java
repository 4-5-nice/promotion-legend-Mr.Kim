package com.wanted.legendkim.global.config;

import com.wanted.legendkim.domain.users.auth.handler.AuthFailHandler;
import com.wanted.legendkim.domain.users.auth.handler.AuthSuccessHandler;
import com.wanted.legendkim.domain.users.user.model.dao.LoginLogRepository;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public AuthSuccessHandler authSuccessHandler(MemberService memberService, LoginLogRepository loginLogRepository) {
        return new AuthSuccessHandler(memberService, loginLogRepository);
    }

    @Bean
    public AuthFailHandler authFailHandler(MemberService memberService, LoginLogRepository loginLogRepository) {
        return new AuthFailHandler(memberService, loginLogRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthSuccessHandler authSuccessHandler,
                                           AuthFailHandler authFailHandler) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/user/enrollments/**", "/user/lectures/**", "/admin/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .requestMatchers("/auth/**", "/user/signup").permitAll()
                        //인증 없이 인가 permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/freeboard/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/questionboard/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/freeboard/user/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/questionboard/user/**").hasAnyAuthority("USER", "ADMIN")
                        //다른 모든 요청은 최소한 로그인이 되어 있어야 접근할 수 있도록 차단
                )
                .exceptionHandling(conf -> conf
                        //메인 페이지에서 "로그인이 필요합니다"라는 알림창을 띄우는 용도 등의 예외처리
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/?error=login_required"))
                )
                //로그인 폼 확인
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        // -> 컨트롤러에서 가로채 로그인 로직 수행
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(authSuccessHandler)
                        .failureHandler(authFailHandler)
                        .permitAll()
                )
                //로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        //로그아웃 시 서버의 세션을 완전히 무효화
                        .invalidateHttpSession(true)
                        //브라우저에 저장된 세션 쿠키를 삭제하여 보안을 강화
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
