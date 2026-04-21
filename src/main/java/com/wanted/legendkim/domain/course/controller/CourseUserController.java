package com.wanted.legendkim.domain.course.controller;
// USER 권한으로 코스 목록을 Json 으로 내려주는 컨트롤러
import com.wanted.legendkim.domain.course.service.CourseService;
import com.wanted.legendkim.domain.course.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @Controller + ResponseBody
@RestController
// final 메서드를 통해 생성자를 주입하기 위해서 사용
@RequiredArgsConstructor
@RequestMapping("/user/courses")
public class CourseUserController {

    // 생성자 주입 이후 재할당 불가능하게 하기 위해서 선언
    private final CourseService courseService;

    // USER 권한으로 코스 목록 조회
    @GetMapping
    /* comment.
        목록 조회 특성 상 다수의 건이 나올 수 있어 컬렉션을 사용했다.
        우리는 이를 통해 DB 결과 순서를 보존하면서 JSON 배열을 내려주기에는
        List 가 제일 이상적이라 생각해 아래처럼 코드를 구성했다.
     */
    public ResponseEntity<List<Course>> getCourseList() {
        List<Course> courses = courseService.getCourseList();
        // 정상적으로 작동하게 되면 200 반환
        return ResponseEntity.ok(courses);
    }
}
