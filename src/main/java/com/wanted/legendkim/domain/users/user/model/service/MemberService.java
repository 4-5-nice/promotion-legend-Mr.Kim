package com.wanted.legendkim.domain.users.user.model.service;

import com.wanted.legendkim.domain.users.user.model.dao.UserRepository;
import com.wanted.legendkim.domain.users.user.model.dto.LoginUserDTO;
import com.wanted.legendkim.domain.users.user.model.dto.PasswordResetDTO;
import com.wanted.legendkim.domain.users.user.model.dto.SignupDTO;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
            //비밀번호는 무조건 암호화
            user.password(encoder.encode(signupDTO.getPassword()));

            User savedUser = userRepository.save(user);
            return savedUser.getUserId();

        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public LoginUserDTO findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Entity를 그대로 밖으로 내보내지 않고 안전한 LoginUserDTO로 변환하여 컨트롤러로 넘김
        return userOptional.map(user -> modelMapper.map(user, LoginUserDTO.class)).orElse(null);
    }

    @Transactional
    public void incrementLoginFailCount(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int newFailCount = user.getLoginFailCount() + 1;

            if (newFailCount >= 5) {
                user.loginFailCount(newFailCount).isLocked(true);
            } else {
                user.loginFailCount(newFailCount);
            }
        }
    }

    @Transactional
    public void resetLoginFailCount(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            userOptional.get().loginFailCount(0);
        }
    }

    //조회용 메서드
    @Transactional(readOnly = true)
    public long getLockedUserCount() {
        return userRepository.countByIsLockedTrue();
    }

    // 잠긴 계정 리스트 조회 (검색어 포함)
    @Transactional(readOnly = true)
    public Page<User> getLockedUsers(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userRepository.findByIsLockedTrueAndNameContaining(keyword, pageable);
        }
        return userRepository.findByIsLockedTrue(pageable);
    }

    @Transactional
    public void unlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
        user.isLocked(false).loginFailCount(0); // 잠금 해제 및 카운트 리셋
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userRepository.findByNameContaining(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    // 비밀번호 재설정 로직 추가
    @Transactional
    public boolean resetPassword(PasswordResetDTO dto) {
        // 비밀번호 불일치 시 즉각 차단
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }

        //optional 쓰는 이유는 가입한 사용자 없으면 그냥 빈 상자 넘기기 위함
        //이 데이터는 DB에 없을 수도 있으니까, 값을 꺼내 쓰기 전에 무조건 한 번 확인해!
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 보안 질문과 답변이 모두 일치하는지 확인
            if (dto.getIdentifyQuestion().equals(user.getIdentifyQuestion()) &&
                    dto.getIdentifyAnswer().equals(user.getIdentifyAnswer())) {

                // 일치한다면 새로운 비밀번호를 암호화하여 업데이트
                user.password(encoder.encode(dto.getNewPassword()));
                return true;
            }
        }
        return false; // 이메일이 없거나 보안정보가 틀린 경우
    }
}
