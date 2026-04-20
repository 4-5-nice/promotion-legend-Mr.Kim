package com.wanted.legendkim.domain.mypage.repository;


import com.wanted.legendkim.domain.mypage.entity.MPAttendance;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<MPAttendance, Integer> {
    List<MPAttendance> findByUserId(MPUsers userId);

    List<MPAttendance> findByUserId_UserId(int userId);

    void deleteByUserId(MPUsers user);
}
