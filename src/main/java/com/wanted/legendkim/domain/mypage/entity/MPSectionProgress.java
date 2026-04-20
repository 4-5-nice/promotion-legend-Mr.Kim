package com.wanted.legendkim.domain.mypage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "section_progress")
public class MPSectionProgress {
    @Id
    @Column(name = "progress_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int progressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private MPEnrollments enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private MPSections sectionId;

    @Column(name = "is_completed")
    private boolean isCompleted;
}
