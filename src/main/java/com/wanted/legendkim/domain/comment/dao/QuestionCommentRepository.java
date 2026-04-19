package com.wanted.legendkim.domain.comment.dao;

import com.wanted.legendkim.domain.comment.entity.QuestionComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionCommentRepository extends JpaRepository<QuestionComment, Long> {

    List<QuestionComment> findByQuestion_IdOrderByCreatedAtAsc(Long questionId);

    void deleteByQuestion_Id(Long questionId);
}
