package com.wanted.legendkim.domain.mypage.repository;


import com.wanted.legendkim.domain.mypage.entity.Attendance;
import com.wanted.legendkim.domain.mypage.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByUserId(Users userId);

    List<Attendance> findByUserId_UserId(int userId);

    void deleteByUserId(Users user);
}
