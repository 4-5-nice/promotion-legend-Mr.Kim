package com.wanted.legendkim.domain.freeboard.dao;

import com.wanted.legendkim.domain.freeboard.entity.FreeBoardPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeBoardPostRepository extends JpaRepository<FreeBoardPost, Long> {

//    // 전체 게시글 조회
//    List<FreeBoardPost> findAllByOrderByCreatedAtDesc();
//
//    // 내가 쓴 글 조회
//    List<FreeBoardPost> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
    select p
    from FreeBoardPost p
    join fetch p.user
    order by p.createdAt desc
""")
    List<FreeBoardPost> findAllWithUserOrderByCreatedAtDesc();

    @Query("""
    select p
    from FreeBoardPost p
    join fetch p.user
    where p.user.id = :userId
    order by p.createdAt desc
""")
    List<FreeBoardPost> findByUserIdWithUserOrderByCreatedAtDesc(@Param("userId") Long userId);

    
}