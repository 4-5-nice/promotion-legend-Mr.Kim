package com.wanted.legendkim.domain.comment.dao;

import com.wanted.legendkim.domain.comment.entity.FreeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeCommentRepository extends JpaRepository<FreeComment, Long> {

//    List<FreeComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    @Query("""
    select c
    from FreeComment c
    join fetch c.user
    where c.post.id = :postId
    order by c.createdAt asc
""")
    List<FreeComment> findByPostIdWithUserOrderByCreatedAtAsc(@Param("postId") Long postId);

    void deleteByPostId(Long postId);
}
