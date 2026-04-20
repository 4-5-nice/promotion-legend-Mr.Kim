package com.wanted.legendkim.domain.board.dao;

import com.wanted.legendkim.domain.board.entity.BoardUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {

    // 로그인한 사용자를 email 기준으로 찾기
    Optional<BoardUser> findByEmail(String email);
}