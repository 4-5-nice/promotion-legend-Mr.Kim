package com.wanted.legendkim.domain.questionboard.dao;

import com.wanted.legendkim.domain.questionboard.entity.BoardQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBoardRepository extends JpaRepository<BoardQuestions, Long> {

    List<BoardQuestions> findAllByOrderByCreatedAtDesc();
}
