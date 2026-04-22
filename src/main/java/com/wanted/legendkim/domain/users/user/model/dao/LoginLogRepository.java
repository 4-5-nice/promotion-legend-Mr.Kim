package com.wanted.legendkim.domain.users.user.model.dao;

import com.wanted.legendkim.domain.users.user.model.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginHistory, Long> {

    // 특정 유저의 로그인 로그를 모두 조회하는 DB 통신 메서드
    List<LoginHistory> findByUserId(Long userId);

    // 전체 시스템의 로그인 로그를 시간 역순으로 조회하여 관리자 모니터링 기능을 지원하는 메서드
    List<LoginHistory> findAllByOrderByCreatedAtDesc();

    // 특정 유저의 로그를 시간 역순으로 조회하여, UI에서 가장 최근 접속 기록부터 보여주기 좋게 정렬해주는 메서드
    List<LoginHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
