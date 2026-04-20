package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.Enrollments;
import com.wanted.legendkim.domain.mypage.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentsRepository extends JpaRepository<Enrollments, Integer> {
    List<Enrollments> findByUserId(Users userId);

    void deleteByUserId(Users user);
}
