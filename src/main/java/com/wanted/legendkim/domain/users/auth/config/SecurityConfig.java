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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

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
                .userDetailsService(authService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .requestMatchers("/auth/**", "/user/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/?error=login_required"))
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email") // login.html의 input name과 일치
                        .passwordParameter("password")
                        .successHandler(authSuccessHandler) // 커스텀 성공 핸들러 추가
                        .failureHandler(authFailHandler)    // 커스텀 실패 핸들러 추가
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