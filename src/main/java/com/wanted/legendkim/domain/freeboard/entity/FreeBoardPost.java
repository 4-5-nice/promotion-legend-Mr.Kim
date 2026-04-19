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

    // 조회수에 1을 더하는 메소드
    public void increaseViewCount() {
        if(this.viewCount == null){
            this.viewCount = 0L; // 혹시 모를 NPE를 막기 위한 코드
        }
        this.viewCount++;
    }

    // DB에 insert 직전에 자동으로 실행되는 어노테이션.(update는 적용 안됨)
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // 현재 시간 찍기
        this.viewCount = 0L; // 혹시 모를 조회수 null 값 방지
    }

    // 변경사항 수정하는 메서드
    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}