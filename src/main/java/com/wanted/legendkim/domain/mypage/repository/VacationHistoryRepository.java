package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.Users;
import com.wanted.legendkim.domain.mypage.entity.VacationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationHistoryRepository extends JpaRepository<VacationHistory, Integer> {
    void deleteByUserId(Users user);
}
