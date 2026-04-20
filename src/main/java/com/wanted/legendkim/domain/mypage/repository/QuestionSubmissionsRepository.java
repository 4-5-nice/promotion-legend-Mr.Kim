package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.QuestionSubmissions;
import com.wanted.legendkim.domain.mypage.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionSubmissionsRepository extends JpaRepository<QuestionSubmissions, Integer> {
    // 1. 특정 유저의 제출 기록을 최신순으로 가져오기
    // findByUserId(Users user) 라고 하면 JPA가 알아서 WHERE user_id = ? 쿼리를 날립니다.
    List<QuestionSubmissions> findByUserIdOrderBySubmittedAtDesc(Users user);

    // 2. 특정 유저가 맞춘 문제 수 세기 (포인트 계산용)
    int countByUserIdAndIsCorrectTrue(Users user);

    // 3. 특정 유저가 틀린 문제 수 세기 (포인트 감점용)
    int countByUserIdAndIsCorrectFalse(Users user);

    void deleteByQuestionId_UserId(Users user);

    void deleteByUserId(Users user);
}
