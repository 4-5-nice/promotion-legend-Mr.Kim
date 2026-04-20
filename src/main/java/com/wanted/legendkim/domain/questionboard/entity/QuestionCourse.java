package com.wanted.legendkim.domain.questionboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "courses")
public class QuestionCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "instructor_name", length = 100)
    private String instructorName;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer duedate;
}