package com.wanted.legendkim.domain.users.auth.model.service;

import com.wanted.legendkim.domain.users.auth.model.dto.AuthDetails;
import com.wanted.legendkim.domain.users.user.model.dto.LoginUserDTO;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService implements UserDetailsService {

    private final MemberService memberService;

    public AuthService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginUserDTO login = memberService.findByEmail(email);

        if(Objects.isNull(login)){
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }
        //해당 린턴구문이 동작을 하게 되면
        //Security Context에 LoginDTO 정보를 담은 Session 인증 객체가 만들어지며
        //우리는 해당 값을 Session이 유지되는 동안 계속 사용할 수 있게 된다.
        return new AuthDetails(login);
    }

}
