package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.MPFreeBoards;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeBoardsRepository extends JpaRepository<MPFreeBoards, Integer> {
    void deleteByUserId(MPUsers user);
}
