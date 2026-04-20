package com.wanted.legendkim.domain.freeboard.dao;

import com.wanted.legendkim.domain.freeboard.entity.FreeBoardUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreeBoardUserRepository extends JpaRepository<FreeBoardUser, Long> {

    // 로그인한 사용자를 email 기준으로 찾기
    Optional<FreeBoardUser> findByEmail(String email);
}