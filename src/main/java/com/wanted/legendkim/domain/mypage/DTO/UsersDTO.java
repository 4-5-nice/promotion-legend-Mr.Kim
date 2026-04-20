package com.wanted.legendkim.domain.mypage.DTO;

import com.wanted.legendkim.domain.mypage.entity.MPAttendance;
import com.wanted.legendkim.domain.mypage.entity.MPPayments;
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

//    private String identifyAnswer;
//    private boolean isPaid;

    // ⬇️ 결제 내역 리스트를 담을 변수 추가!
    private List<MPPayments> payments;
    // 출결 정보 담을 변수
    private List<MPAttendance> attendance;

   
    public UsersDTO(String name, String email, int point, String rank, int vacationCoupon, List<MPPayments> payments, List<MPAttendance> attendance) {
        this.name = name;
        this.email = email;
        this.point = point;
        this.rank = rank;
        this.vacationCoupon = vacationCoupon;
        this.payments = payments;
        this.attendance = attendance;
    }

    public UsersDTO(String name, String email, int point, String rank, int vacationCoupon, List<MPPayments> paymentsList) {

        this.name = name;
        this.email = email;
        this.point = point;
        this.rank = rank;
        this.vacationCoupon = vacationCoupon;
        this.payments = payments;

    }
}
