package com.wanted.legendkim.domain.questionboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "upload_success", nullable = false)
    private Boolean uploadSuccess;
}