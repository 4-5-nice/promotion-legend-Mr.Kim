package com.wanted.legendkim.domain.course;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository <엔티티, PK> 상속
// 상속 진행 시 기본 CRUD 자동 생성
public interface CourseRepository extends JpaRepository<Course, Long> {



}
