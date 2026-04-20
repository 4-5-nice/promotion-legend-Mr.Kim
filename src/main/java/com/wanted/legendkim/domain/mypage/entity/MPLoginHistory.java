package com.wanted.legendkim.domain.mypage.entity;

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
@Entity
@Table(name = "login_history")
public class MPLoginHistory {
    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MPUsers userId;

    @Column(name = "is_success")
    private boolean isSuccess;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
