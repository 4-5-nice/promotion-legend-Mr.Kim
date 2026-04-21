package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.MPLoginHistory;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import com.wanted.legendkim.domain.users.user.model.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LoginHistoryRepository extends JpaRepository<MPLoginHistory, Integer> {
    void deleteByUserId(MPUsers user);

    Optional<MPLoginHistory> findFirstByUserIdAndIsSuccessAndCreatedAtAfterOrderByCreatedAtAsc(MPUsers user, boolean isSuccess, LocalDateTime CreatedAt);
}
