package com.wanted.legendkim.domain.users.auth.model.service;

import com.wanted.legendkim.domain.users.auth.model.dto.AuthDetails;
import com.wanted.legendkim.domain.users.user.model.dao.UserRepository;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new AuthDetails(user);
    }
}
