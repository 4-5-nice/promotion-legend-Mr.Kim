package com.wanted.legendkim.domain.course;

import com.wanted.legendkim.domain.section.Section;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Section 엔티티와 동일한 이유로 @Setter 와
// @AllArgsConstructor 미사용
@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor


public class Course {

    @Id
    // 코스가 추가될 때마다 PK값을 자동으로 증가시켜서 JPA에 알려준다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    // Course 1개에 section 여러 개, 관계 주인은 Section 성능을 위해 Lazy
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    // 여러 Section 을 담아야하기 때문에 리스트 사용
    private List<Section> sections = new ArrayList<>();

    // user_id 컬럼 설명
    @Column(name = "user_id")
    private Long userId;

    // title 컬럼 설정
    @Column(nullable = false)
    private String title;

    // instructor_name 컬럼 설정
    @Column(name = "instructor_name")
    private String insName;

    // description 컬럼 설정
    @Column(name = "description")
    private String description;

    // duedate 컬럼 설정
    @Column(name = "duedate")
    private int dueDate;

    public static Course create(String title, String description) {

        Course course = new Course();
        course.title = title;
        course.description = description;
        return course;

    }

}