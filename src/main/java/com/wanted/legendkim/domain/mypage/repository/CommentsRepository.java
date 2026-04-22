package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.MPComments;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<MPComments, Integer> {
    void deleteByPostId_UserId(MPUsers user);

    void deleteByQuestionId_UserId(MPUsers user);

    void deleteByUserId(MPUsers user);
}
