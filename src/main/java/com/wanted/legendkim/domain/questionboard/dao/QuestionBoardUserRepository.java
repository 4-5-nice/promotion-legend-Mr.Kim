package com.wanted.legendkim.domain.questionboard.dao;

import com.wanted.legendkim.domain.questionboard.entity.QuestionBoardUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionBoardUserRepository extends JpaRepository<QuestionBoardUser, Long> {

    Optional<QuestionBoardUser> findByEmail(String email);
}
