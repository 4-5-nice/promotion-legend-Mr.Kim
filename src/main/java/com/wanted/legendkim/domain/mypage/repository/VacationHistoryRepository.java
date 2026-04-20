package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import com.wanted.legendkim.domain.mypage.entity.MPVacationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationHistoryRepository extends JpaRepository<MPVacationHistory, Integer> {
    void deleteByUserId(MPUsers user);
}
