package com.wanted.legendkim.domain.users.user.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "role", nullable = false, length = 20)
    @ColumnDefault("'USER'")
    private String role = "USER";

    @Column(name = "is_paid", nullable = false)
    @ColumnDefault("false")
    private Boolean isPaid = false;

    @Column(name = "point")
    @ColumnDefault("0")
    private Integer point = 0;

    @Column(name = "`rank`", nullable = false, length = 20)
    @ColumnDefault("'인턴'")
    private String rank = "인턴";

    @Column(name = "login_fail_count")
    @ColumnDefault("0")
    private Integer loginFailCount = 0;

    @Column(name = "is_locked")
    @ColumnDefault("false")
    private Boolean isLocked = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "vacation_coupon")
    @ColumnDefault("0")
    private Integer vacationCoupon = 0;

    @Column(name = "identify_question", length = 100)
    private String identifyQuestion;

    @Column(name = "identify_answer", length = 100)
    private String identifyAnswer;

    public User(){}

    public User(String email, String password, String name, LocalDate birthDate, String role, Integer point, String rank, Integer loginFailCount, Boolean isLocked, LocalDateTime createdAt, LocalDateTime deletedAt, Integer vacationCoupon, String identifyQuestion) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.role = role;
        this.point = point;
        this.rank = rank;
        this.loginFailCount = loginFailCount;
        this.isLocked = isLocked;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.vacationCoupon = vacationCoupon;
        this.identifyQuestion = identifyQuestion;
    }

    // --- Builder methods for chaining (수동 체이닝 메서드) ---
    public User email(String email) {
        this.email = email;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

    public User birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public User role(String role) {
        this.role = role;
        return this;
    }

    public User isPaid(Boolean isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public User point(Integer point) {
        this.point = point;
        return this;
    }

    public User rank(String rank) {
        this.rank = rank;
        return this;
    }

    public User loginFailCount(Integer loginFailCount) {
        this.loginFailCount = loginFailCount;
        return this;
    }

    public User isLocked(Boolean isLocked) {
        this.isLocked = isLocked;
        return this;
    }

    public User vacationCoupon(Integer vacationCoupon) {
        this.vacationCoupon = vacationCoupon;
        return this;
    }

    public User identifyQuestion(String identifyQuestion) {
        this.identifyQuestion = identifyQuestion;
        return this;
    }

    public User identifyAnswer(String identifyAnswer) {
        this.identifyAnswer = identifyAnswer;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getRole() {
        return role;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public Integer getPoint() {
        return point;
    }

    public String getRank() {
        return rank;
    }

    public Integer getLoginFailCount() {
        return loginFailCount;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public Integer getVacationCoupon() {
        return vacationCoupon;
    }

    public String getIdentifyQuestion() {
        return identifyQuestion;
    }

    public String getIdentifyAnswer() {return identifyAnswer;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", role='" + role + '\'' +
                ", point=" + point +
                ", rank='" + rank + '\'' +
                ", loginFailCount=" + loginFailCount +
                ", isLocked=" + isLocked +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                ", vacationCoupon=" + vacationCoupon +
                ", identifyQuestion='" + identifyQuestion + '\'' +
                '}';
    }
}


