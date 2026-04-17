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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public FreeComment(FreeBoardPost post, FreeBoardUser user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
