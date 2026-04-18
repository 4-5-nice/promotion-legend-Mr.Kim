package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.FreeBoards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeBoardsRepository extends JpaRepository<FreeBoards, Integer> {
}
