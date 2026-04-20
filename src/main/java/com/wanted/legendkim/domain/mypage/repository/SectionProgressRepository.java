package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.SectionProgress;
import com.wanted.legendkim.domain.mypage.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionProgressRepository extends JpaRepository<SectionProgress, Integer> {
    void deleteByEnrollmentId_UserId(Users user);
}
