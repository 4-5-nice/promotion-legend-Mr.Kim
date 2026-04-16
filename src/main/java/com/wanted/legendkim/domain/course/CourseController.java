package com.wanted.legendkim.domain.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Http 요청을 JSON 형태로 응답하는 컨트롤러를 Spring 에 전달
@RestController
// courseService 를 주입해주기 위해 사용
@RequiredArgsConstructor
// API 명세서 앞 부분 URL
@RequestMapping("/admin/courses")
public class CourseController {

    // final 선언해주기
    private final CourseService courseService;

    // API 명세서의 나머지 URL (POST 로 지정)
    @PostMapping

    /* comment.
        ResponseEntity 는 HTTP 응답 전체를 제어할 수 있는 객체이다.
        <String> 은 응답을 문자열 메세지에 담는다는 뜻이다.
     */
    public ResponseEntity<String> courseInput(
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        courseService.registerCourse(title, description);
        return ResponseEntity.ok("✅코스 등록를 성공했습니다!✅");
    }

    /* comment. 흐름도
        1. GET 요청을 받는다.
        2. 서비스에서 데이터를 가져온다.
        3. 성공 응답을 반환한다.
     */

    // 코스 등록을 위해서 작성한 로직
    @GetMapping
    // ResponseEintity 는 HTTP 응답 전체를 통제.
    // 우리는 컨트롤러 안에서 비즈니스 로직을 처리하지 않으며, ControllerService 에서
    // 비즈니스 로직을 처리한다.
    public ResponseEntity<List<Course>> getCourseList() {
        List<Course> courses = courseService.getCourseList();
        return ResponseEntity.ok(courses);
    }
}
