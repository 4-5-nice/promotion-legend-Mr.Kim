package com.wanted.legendkim.domain.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanted.legendkim.domain.section.Section;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Section 엔티티와 동일한 이유로 @Setter 와
// @AllArgsConstructor 미사용
@Entity // DB 와 매핑하는 클래스를 선언
@Table(name = "courses") // DB 에 실제 생성될 때 테이블을 courses 로 명명
@Getter
@NoArgsConstructor


public class Course {

    @Id
    // 코스가 추가될 때마다 PK값을 자동으로 증가시켜서 JPA에 알려준다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    /* comment.
        JsonIgnore
        우리의 프로젝트에서는 Course -> Section -> Course 로 연결이 되면
        JSON 변환 시 순환 참조가 발생하게 된다.
        이를 위해 우리는 @JsonIgnore 어노테이션을 사용하게 된다.
     */

    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
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

    @Column(name = "track")
    private String track;

    // 비즈니스 로직 캡슐화
    // 우리는 이를 통해서 규칙이 추가되었을 경우 이 메서드만 수정하면 된다.
    public static Course create(String title, String description, int dueDate, String track) {

        Course course = new Course();
        course.title = title;
        course.description = description;
        course.dueDate = dueDate;
        course.track = track;
        return course;

    }

}