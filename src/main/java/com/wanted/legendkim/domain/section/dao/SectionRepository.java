package com.wanted.legendkim.domain.section.dao;

import com.wanted.legendkim.domain.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository<엔티티, PK> 상속
// 상속 진행 시 기본 CRUD 자동 생성
public interface SectionRepository extends JpaRepository<Section, Long> {

    // courseId 로 해당 코스의 섹션 목록 조회
    List<Section> findByCourseId(Long courseId);
}
