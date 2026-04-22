package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.MPEnrollments;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentsRepository extends JpaRepository<MPEnrollments, Integer> {
    List<MPEnrollments> findByUserId(MPUsers userId);

    void deleteByUserId(MPUsers user);
}
