package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.Courses;
import com.wanted.legendkim.domain.mypage.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Integer> {
    List<Courses> findByUserId(Users userId);
}
