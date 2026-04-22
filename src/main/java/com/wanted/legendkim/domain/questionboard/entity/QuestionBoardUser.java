package com.wanted.legendkim.domain.questionboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class QuestionBoardUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false)
    private Integer point;

    @Convert(converter = RankConverter.class)
    @Column(name = "`rank`", nullable = false, length = 20)
    private Rank rank;

    @Column(name = "login_fail_count", nullable = false)
    private Integer loginFailCount;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "vacation_coupon", nullable = false)
    private Integer vacationCoupon;

    @Column(name = "identify_question", length = 100)
    private String identifyQuestion;

    @Column(name = "identify_answer", length = 100)
    private String identifyAnswer;

    // 점수를 반영하는 메서드
    public void addPoint(int amount) {
        if (this.point == null) {
            this.point = 0;
        }
        this.point += amount;
        if (this.point < 0) {
            this.point = 0;
        }
        this.rank = Rank.fromPoint(this.point); // 포인트를 기반으로 직급 변경
    }
}
