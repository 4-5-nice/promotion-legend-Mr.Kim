package com.wanted.legendkim.domain.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/courses")
public class CourseUserController {

    private final CourseService courseService;

    // USER 권한으로 코스 목록 조회
    @GetMapping
    public ResponseEntity<List<Course>> getCourseList() {
        List<Course> courses = courseService.getCourseList();
        return ResponseEntity.ok(courses);
    }
}
