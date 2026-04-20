package com.wanted.legendkim.domain.questionboard.dao;

import com.wanted.legendkim.domain.questionboard.entity.QuestionCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionCourseRepository extends JpaRepository<QuestionCourse, Long> {

    List<QuestionCourse> findAllByOrderByIdAsc();
}
