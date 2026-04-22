package com.wanted.legendkim.domain.questionboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "QUESTIONS")
public class BoardQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private QuestionBoardUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private QuestionCourse course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private QuestionSection section;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "option1", nullable = false, length = 255)
    private String option1;

    @Column(name = "option2", nullable = false, length = 255)
    private String option2;

    @Column(name = "option3", nullable = false, length = 255)
    private String option3;

    @Column(name = "option4", nullable = false, length = 255)
    private String option4;

    @Column(name = "option5", nullable = false, length = 255)
    private String option5;

    @Column(nullable = false)
    private Integer answer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "view_count")
    private Long viewCount;

    public BoardQuestions(
            QuestionBoardUser user,
            QuestionCourse course,
            QuestionSection section,
            String title,
            String option1,
            String option2,
            String option3,
            String option4,
            String option5,
            Integer answer
    ) {
        this.user = user;
        this.course = course;
        this.section = section;
        this.title = title;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.answer = answer;
    }

    // DB에 insert 직전에 자동으로 실행되는 어노테이션.(update는 적용 안됨)
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // 현재 날짜 찍기
        }
        if (this.viewCount == null) {
            this.viewCount = 0L; // 조회수는 0으로 세팅
        }
    }
}