package com.wanted.legendkim.domain.mypage.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties({"userId", "hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "attendance")
public class MPAttendance {

    @Id
    @Column(name = "attendance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MPUsers userId;

    @Column(name = "target_date")
    private LocalDateTime targetDate;

    @Column(name = "status", columnDefinition = "ENUM('PRESENT', 'LATE', 'ABSENT', 'EXCUSED')")
    private String status;


    // Attendance.java 엔티티
    public MPAttendance changeStatus(String status) {
        this.status = status;
        return this; // 빌더처럼 체이닝하기 위해 자기 자신 반환
    }

    // Attendance.java 에 추가
    public MPAttendance fillDetails(MPUsers userId, LocalDateTime date, String status) {
        this.userId = userId;
        this.targetDate = date;
        this.status = status;
        return this;
    }

    public MPAttendance(MPUsers user, LocalDateTime loginTime, String status) {
        this.userId = user;
        this.targetDate = loginTime;
        this.status = status;
    }
}
