package com.wanted.legendkim.domain.board.dao;

import com.wanted.legendkim.domain.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 게시글 조회
    List<Post> findAllByOrderByCreatedAtDesc();

    // 내가 쓴 글 조회
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    
}