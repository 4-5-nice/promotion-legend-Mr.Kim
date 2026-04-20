package com.wanted.legendkim.domain.mypage.DTO;

import com.wanted.legendkim.domain.mypage.entity.Attendance;
import com.wanted.legendkim.domain.mypage.entity.Payments;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UsersDTO {
    private int userId;
    private String email;
    private String password;
    private String name;
    private String role;
    private int point;
    private String rank;
    private int loginFailCount;
    private boolean isLocked;
    private Date createdAt;
    private Date deletedAt;
    private int vacationCoupon;
    private String identifyQuestion;

    // ⬇️ 결제 내역 리스트를 담을 변수 추가!
    private List<Payments> payments;
    // 출결 정보 담을 변수
    private List<Attendance> attendance;

    public UsersDTO(String name, String email, int point, String rank, int vacationCoupon, List<Payments> payments) {
        this.name = name;
        this.email = email;
        this.point = point;
        this.rank = rank;
        this.vacationCoupon = vacationCoupon;
        this.payments = payments;
    }

    public UsersDTO(String name, String email, int point, String rank, int vacationCoupon, List<Payments> payments, List<Attendance> attendance) {
        this.name = name;
        this.email = email;
        this.point = point;
        this.rank = rank;
        this.vacationCoupon = vacationCoupon;
        this.payments = payments;
        this.attendance = attendance;
    }
}
