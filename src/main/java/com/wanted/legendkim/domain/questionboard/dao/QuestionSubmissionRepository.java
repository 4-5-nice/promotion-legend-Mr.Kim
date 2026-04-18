package com.wanted.legendkim.domain.questionboard.dao;

import com.wanted.legendkim.domain.questionboard.entity.QuestionSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionSubmissionRepository extends JpaRepository<QuestionSubmission, Long> {

    Optional<QuestionSubmission> findByQuestion_IdAndUser_Id(Long questionId, Long userId);

    boolean existsByQuestion_IdAndUser_Id(Long questionId, Long userId);
}
