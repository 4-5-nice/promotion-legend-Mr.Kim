package com.wanted.legendkim.domain.mypage.service;

import com.wanted.legendkim.domain.mypage.entity.MPQuestionSubmissions;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import com.wanted.legendkim.domain.mypage.repository.QuestionSubmissionsRepository;
import com.wanted.legendkim.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionSubmissionsService {
    private final QuestionSubmissionsRepository submissionRepository;
    private final UsersRepository userRepository; // 유저 엔티티 가져오기용

    public Map<String, Object> getQuizInfo(String email) {
        // 유저 객체 확보
        MPUsers user = userRepository.findByEmail(email).get(0);

        // 데이터 가져오기
        List<MPQuestionSubmissions> history = submissionRepository.findByUserIdOrderBySubmittedAtDesc(user);
        int correctCount = submissionRepository.countByUserIdAndIsCorrectTrue(user);
        int incorrectCount = submissionRepository.countByUserIdAndIsCorrectFalse(user);

        // 맞으면 +1, 틀리면 -1
        int totalPoints = correctCount - incorrectCount;

        Map<String, Object> stats = new HashMap<>();
        stats.put("history", history);
        stats.put("totalSolved", history.size()); // 총 시도 횟수
        stats.put("totalPoints", totalPoints);    // 최종 LP

        return stats;
    }
}
