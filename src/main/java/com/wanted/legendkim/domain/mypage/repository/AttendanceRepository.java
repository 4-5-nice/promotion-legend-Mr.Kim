package com.wanted.legendkim.domain.mypage.repository;


import com.wanted.legendkim.domain.mypage.entity.MPAttendance;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<MPAttendance, Integer> {
    List<MPAttendance> findByUserId(MPUsers userId);

    List<MPAttendance> findByUserId_UserId(int userId);

    void deleteByUserId(MPUsers user);


    Optional<MPAttendance> findByUserIdAndTargetDate(MPUsers user, LocalDateTime today);
}
