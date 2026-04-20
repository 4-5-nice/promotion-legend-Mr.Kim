package com.wanted.legendkim.domain.questionboard.dao;

import com.wanted.legendkim.domain.questionboard.entity.QuestionSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionSectionRepository extends JpaRepository<QuestionSection, Long> {

    List<QuestionSection> findByCourse_IdOrderByIdAsc(Long courseId);

    Optional<QuestionSection> findByIdAndCourse_Id(Long sectionId, Long courseId);
}
