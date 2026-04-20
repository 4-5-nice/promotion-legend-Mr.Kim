package com.wanted.legendkim.domain.comment.dao;

import com.wanted.legendkim.domain.comment.entity.FreeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeCommentRepository extends JpaRepository<FreeComment, Long> {

    List<FreeComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    void deleteByPostId(Long postId);
}
