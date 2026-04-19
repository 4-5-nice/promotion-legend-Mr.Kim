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

        // 엔티티를 DTO로 변환 (ModelMapper 사용)
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
        // 백엔드 단에서도 비밀번호 일치 여부 한 번 더 검증
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }

        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 보안 질문과 답변이 모두 일치하는지 확인
            if (dto.getIdentifyQuestion().equals(user.getIdentifyQuestion()) &&
                    dto.getIdentifyAnswer().equals(user.getIdentifyAnswer())) {

                // 일치한다면 새로운 비밀번호를 암호화하여 플루언트 방식으로 업데이트 (Dirty Checking)
                user.password(encoder.encode(dto.getNewPassword()));
                return true;
            }
        }
        return false; // 이메일이 없거나 보안정보가 틀린 경우
    }
}
