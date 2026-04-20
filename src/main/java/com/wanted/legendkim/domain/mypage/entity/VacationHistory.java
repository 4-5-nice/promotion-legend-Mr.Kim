package com.wanted.legendkim.domain.mypage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "vacation_history")
public class VacationHistory {
    @Id
    @Column(name = "vacation_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vacationHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users userId;

    @Column(name = "used_date")
    private Date usedDate;

    @Column(name = "deducted_amount")
    private int deductedAmount;

    @Column(name = "purpose", columnDefinition = "ENUM('ETC', 'SICK', 'SELF_IMPROVEMENT'")
    private String purpose; //기타, 병결, 자기계발

    @Column(name = "detail_purpose")
    private String detailPurpose;

    // VacationHistory.java 에 추가
    public VacationHistory fillDetails(Users user, Date date, int amount, String purpose, String detailPurpose) {
        this.userId = user;
        this.usedDate = date;
        this.deductedAmount = amount;
        this.purpose = purpose;
        this.detailPurpose = detailPurpose;
        return this; // 👈 이게 핵심! 그래야 연달아 쓸 수 있습니다.
    }

}
