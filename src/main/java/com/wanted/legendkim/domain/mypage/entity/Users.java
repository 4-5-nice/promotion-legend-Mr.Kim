package com.wanted.legendkim.domain.mypage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "point")
    private int point;

    @Column(name = "`rank`")
    private String rank;

    @Column(name = "login_fail_count")
    private int loginFailCount;

    @Column(name = "is_locked")
    private boolean isLocked;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "vacation_coupon")
    private int vacationCoupon;

    @Column(name = "identify_question")
    private String identifyQuestion;

    // Users.java
    public void useVacation(int count) {
        // 여기서 직접 계산해서 업데이트!
        this.vacationCoupon -= count;
    }
}
