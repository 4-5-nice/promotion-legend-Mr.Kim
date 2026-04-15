package com.wanted.legendkim.domain.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Spring 에서 이 클래스를 섭미스 레이어로 인식
@Service
// final 필드를 생성자로 주입
@RequiredArgsConstructor
public class CourseService {

    // Course > Section 이니 Course만 주입
    private final CourseRepository courseRepository;

    public void registerCourse(String title, String description) {

        Course course = Course.create(title, description);
        courseRepository.save(course);

    }
}
