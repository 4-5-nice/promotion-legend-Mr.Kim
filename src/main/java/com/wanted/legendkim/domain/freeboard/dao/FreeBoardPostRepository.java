package com.wanted.legendkim.domain.freeboard.dao;

import com.wanted.legendkim.domain.freeboard.entity.FreeBoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardPostRepository extends JpaRepository<FreeBoardPost, Long> {

    // 전체 게시글 조회
    List<FreeBoardPost> findAllByOrderByCreatedAtDesc();

    // 내가 쓴 글 조회
    List<FreeBoardPost> findByUserIdOrderByCreatedAtDesc(Long userId);

    
}