package com.wanted.legendkim.domain.section;


import com.wanted.legendkim.domain.course.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sections")
@Getter
@NoArgsConstructor

/* comment.
    @AllArgsConstructor 미사용 이유
    - 필드 순서에 의존하는 생성자는 필드 추가/변경 시 모든 호출부 수정 필요
    - 외부에서 엔티티를 무분별하게 생성하는 것을 막기 위해 미사용
    - 객체 생성은 정적 메서드로 통제
    +
    @Setter 미사용 이유
    - 엔티티의 값이 어디서든 변경될 수 있으면 추적이 어려워지기 때문.
    - 값 변경이 필요한 경우 명시적인 메서드를 통해서만 변경
 */

public class Section {

    @Id
    // 섹션이 추가될 때마다 PK값을 DB가 자동으로 증가시켜서 JPA에 알려줘야하기
    // 때문에 아래의 어노테이션 추가 작성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    // DB에서 BigInt를 사용했기 때문에 Long 타입 작성
    private Long id;

    /* comment.
        DB 에서는 course_id 숫자 하나로 관계를 표현하지만,
        Java 에서는 객체로 관계를 표현해야한다.
        따라서 이 둘을 연결해주는 다리 역할이 @ManyToOne 과 @JoinColumn 역할이다.
        *
        @ManyToOne
        - 하나의 Course에 여러 Section 이 속하는 N:1 관계를 선언
        - 이 선언이 없다면 section.getCourse()  .getTitle() 같은
        - 객체 탐색이 불가능하며, courseId 로 DB를 전체 조회해야하기 때문이다.
        *
        fetch - lazy
        - section 조회 시 Course 데이터를 즉시 가져오지 않고
        - 실제로 getCourse() 를 호출하는 시점에서 DB에서 가져온다.
        - EAGER 방식은 불필요한 쿼리가 발생하기 때문에 선택하지 않았다.
        *
        @JoinColumn
        - DB 의 Course_id 컬럼을 FK로 사용해서 COURSES 테이블과 연결.
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // title 컬럼 설정
    @Column(nullable = false)
    private String title;

    // 영상 URL 컬럼 설정
    @Column(name = "video_url")
    private String videoUrl;

    // 업로드 성공 여부 컬럼 설정
    @Column(name = "upload_success")
    private Boolean uploadSuccess;

    // @AllArgsConstructor 와 @Setter 가 없는 상황이기 때문에
    // 우리는 객체를 안전하게 생성하기 위해 static 메서드를 사용한다.
    public static Section create(Course course, String title, String fileName) {

        Section section = new Section();
        section.course = course;
        section.title = title;
        section.videoUrl = fileName;
        // uploadSuccess 는 생성 시점에서 항상 False 여야만 하는 규칙이라 내부에서 고정 한다.
        section.uploadSuccess = false;
        return section;
    }
}
