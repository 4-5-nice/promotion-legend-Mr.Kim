package com.wanted.legendkim.domain.comment.entity;

import com.wanted.legendkim.domain.freeboard.entity.FreeBoardUser;
import com.wanted.legendkim.domain.freeboard.entity.FreeBoardPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class FreeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private FreeBoardPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private FreeBoardUser user;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist // DB에 insert 직전에 자동으로 실행되는 어노테이션.(update는 적용 안됨)
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // 현재 시간 찍기
    }

    public FreeComment(FreeBoardPost post, FreeBoardUser user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }

    // 댓글 변경하는 메서드
    public void modify(String content) {
        this.content = content;
    }
}
