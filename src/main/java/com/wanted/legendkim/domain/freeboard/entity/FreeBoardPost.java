package com.wanted.legendkim.domain.freeboard.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "free_boards")
public class FreeBoardPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private FreeBoardUser user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "view_count")
    private Long viewCount;

    public FreeBoardPost(FreeBoardUser user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public void increaseViewCount() {
        if(this.viewCount == null){
            this.viewCount = 0L;
        }
        this.viewCount++;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0L;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}