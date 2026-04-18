package com.wanted.legendkim.domain.comment.entity;

import com.wanted.legendkim.domain.questionboard.entity.QuestionBoardUser;
import com.wanted.legendkim.domain.questionboard.entity.Questions;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class QuestionComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private QuestionBoardUser user;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public QuestionComment(Questions question, QuestionBoardUser user, String content) {
        this.question = question;
        this.user = user;
        this.content = content;
    }

    @PrePersist // DB에 insert 되기 전에 자동으로 실행되는 annotation
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // 현재 시간 찍기
        }
    }
}
