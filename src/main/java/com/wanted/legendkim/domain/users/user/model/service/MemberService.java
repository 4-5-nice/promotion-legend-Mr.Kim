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

        // 1. 중복 이메일 체크 (Unique 키 기준)
        if (userRepository.existsByEmail(signupDTO.getEmail())) {
            return null;
        }
        try {
            // 2. ModelMapper를 사용하여 DTO -> Entity 자동 변환
            User user = modelMapper.map(signupDTO, User.class);

            // 3. 비밀번호는 암호화 처리 후 재할당 (플루언트 세터 활용)
            user.password(encoder.encode(signupDTO.getPassword()));

            // 4. DB 저장 후 생성된 PK(userId) 반환
            User savedUser = userRepository.save(user);
            return savedUser.getUserId();

        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
