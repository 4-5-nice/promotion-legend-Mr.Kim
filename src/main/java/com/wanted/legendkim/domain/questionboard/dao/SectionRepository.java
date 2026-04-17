package com.wanted.legendkim.domain.questionboard.dao;

import com.wanted.legendkim.domain.questionboard.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.wanted.legendkim.domain.questionboard.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByCourse_IdOrderByIdAsc(Long courseId);

    Optional<Section> findByIdAndCourse_Id(Long sectionId, Long courseId);
}
