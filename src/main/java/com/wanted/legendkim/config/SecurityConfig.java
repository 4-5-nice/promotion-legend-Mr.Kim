package com.wanted.legendkim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Spring 설정 클래스임을 선언
@Configuration
// Spring Security 활성화
@EnableWebSecurity
public class SecurityConfig {

    // ======================================
    // 임시 Security 설정 (개발용 - 배포 전 반드시 수정)
    // ======================================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF: 위조 요청 방지 기능 - 임시로 꺼둠 (파일 업로드 API 호출 허용을 위해)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/video/**").permitAll()
                .anyRequest().authenticated()
            )
            // Spring 기본 로그인 폼 사용
            .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
