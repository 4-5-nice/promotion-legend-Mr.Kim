package com.wanted.legendkim.domain.users.user.model.service;

import com.wanted.legendkim.domain.users.user.model.dao.UserRepository;
import com.wanted.legendkim.domain.users.user.model.dto.SignupDTO;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper; // ModelMapper 주입

    @Transactional
    public Long regist(SignupDTO signupDTO) {

        if (userRepository.existsByEmail(signupDTO.getEmail())) {
            return null;
        }
        try {
            User user = modelMapper.map(signupDTO, User.class);
            user.password(encoder.encode(signupDTO.getPassword()));

            User savedUser = userRepository.save(user);
            return savedUser.getUserId();

        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
