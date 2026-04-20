package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.MPSectionProgress;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionProgressRepository extends JpaRepository<MPSectionProgress, Integer> {
    void deleteByEnrollmentId_UserId(MPUsers user);
}
