package com.wanted.legendkim.domain.users.user.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId; // PK: BIGINT -> Long

    // 💡 Tip: 지금은 단순 숫자(Long)로 매핑했지만,
    // 나중에 User 객체를 직접 참조하고 싶다면 @ManyToOne을 사용할 수 있습니다.
    @Column(name = "user_id", nullable = false)
    private Long userId; // FK: BIGINT -> Long

    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess;

    @Column(name = "fail_reason", length = 50)
    private String failReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- Builder methods for chaining (수동 체이닝 메서드) ---

    public LoginHistory userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LoginHistory isSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }

    public LoginHistory failReason(String failReason) {
        this.failReason = failReason;
        return this;
    }

    public LoginHistory(){

    }

    public LoginHistory(Long userId, Boolean isSuccess, String failReason, LocalDateTime createdAt) {
        this.userId = userId;
        this.isSuccess = isSuccess;
        this.failReason = failReason;
        this.createdAt = createdAt;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getFailReason() {
        return failReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "LoginHistory{" +
                "historyId=" + historyId +
                ", userId=" + userId +
                ", isSuccess=" + isSuccess +
                ", failReason='" + failReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

