package com.wanted.legendkim.domain.course.service;

import com.wanted.legendkim.domain.course.dao.CourseRepository;
import com.wanted.legendkim.domain.course.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Spring 에서 이 클래스를 서비스 레이어로 인식
@Service
// final 필드를 생성자로 주입
@RequiredArgsConstructor
public class CourseService {

    // Course > Section 이니 Course만 주입
    private final CourseRepository courseRepository;

    // 코스 등록 코드 블록
    public void registerCourse(String title, String description, int dueDate) {

        Course course = Course.create(title, description, dueDate);
        courseRepository.save(course);
    }

    // 영상 등록 페이지에서 목록 조회 리스트
    // 쿼리 작성을 하지 않고 DB의 모든 데이터를 가져온다.
    public List<Course> getCourseList() {
        return courseRepository.findAll();
    }

}
