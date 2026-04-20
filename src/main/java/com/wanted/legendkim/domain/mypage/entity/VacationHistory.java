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
    private String purpose;


}
