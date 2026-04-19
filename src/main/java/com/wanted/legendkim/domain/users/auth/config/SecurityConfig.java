package com.wanted.legendkim.domain.users.auth.config;

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

    // 💡 에러 원인 1: AuthService 직접 주입 제거 (Spring Security가 구현체를 자동 감지합니다)

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
                .authorizeHttpRequests(auth -> auth
                        // 💡 에러 원인 2 해결: WebSecurityCustomizer 대신 여기서 정적 리소스 접근 허용
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .requestMatchers("/auth/**", "/user/signup").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/?error=login_required"))
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(authSuccessHandler)
                        .failureHandler(authFailHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}