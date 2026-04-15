package com.wanted.legendkim.domain.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            // @PathVariable을 사용한 이유는 URL 경로를 받아오기 위해서 사용했다.
            // 하지만 여기서는 받아올 URL 이 없기 때문에 @RequestParam 만 있으면 된다.
            // 이를 통해서 API 명세서 기준의 값이 Body 로 전달되기 때문에 사용한다.
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        // Controller 는 요청을 받는 역할을 한다. 실제 코스 등록
        // 비즈니스 로직은 Service 에서 진행한다.
        courseService.registerCourse(title, description);
        // 성공시 200이 나오게 한다.
        return ResponseEntity.ok("✅코스 등록를 성공했습니다!✅");
    }


}
