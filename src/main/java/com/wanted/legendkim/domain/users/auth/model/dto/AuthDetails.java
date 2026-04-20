package com.wanted.legendkim.domain.users.auth.model.dto;

import com.wanted.legendkim.domain.users.user.model.dto.LoginUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class AuthDetails implements UserDetails {

    private LoginUserDTO loginUser;

    public AuthDetails(LoginUserDTO loginUser) {
        this.loginUser = loginUser;
    }

    public Long getUserId() {
        return loginUser.getUserId();
    }

    public String getEmail() {
        return loginUser.getEmail();
    }

    /**
     * 권한 정보를 반환하는 메서드이다.
     * UsernamePasswordAuthenticationToken에 사용자의 권한 정보를 넣을 때 사용된다.
     * */
    //이거는 권한 여러개 부여 할 수 있도록 하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        loginUser.getRole().forEach(role -> authorities.add(() -> role));

        return authorities;
    }

    //타임리프에서 사용하기 위한 메서드 정의
    @Override
    public String getPassword() {
        return loginUser.getPassword();
    }

    @Override
    public String getUsername() {
        return loginUser.getEmail();
    }

    public String getName() {
        return loginUser.getName();
    }

    public LoginUserDTO getLoginUser() {
        return loginUser;
    }

    public Boolean getIsPaid() {
        return loginUser.getIsPaid();
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부 확인
        return !Boolean.TRUE.equals(loginUser.getIsLocked());
    }

    //사용자 같은지 확인
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthDetails that)) return false;

        // userId 기준으로 동일 사용자 판단
        return Objects.equals(this.getEmail(), that.getEmail());
    }
    //hashcode는 반드시 equals와 함꼐 오버라이딩 userId 기준으로 오버라이딩
    //이유는 하나는 userId, 하나는 DTO이면 equals 무조건 false기 떄문
    @Override
    public int hashCode() {
        return Objects.hash(this.getEmail());
    }

}
