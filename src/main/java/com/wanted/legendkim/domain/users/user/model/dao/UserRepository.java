package com.wanted.legendkim.domain.users.user.model.dao;

import com.wanted.legendkim.domain.users.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    long countByIsLockedTrue();

    // 잠긴 계정 중 이름 검색 + 페이징 처리
    Page<User> findByIsLockedTrueAndNameContaining(String name, Pageable pageable);

    // 잠긴 계정 전체 조회 + 페이징 처리
    Page<User> findByIsLockedTrue(Pageable pageable);

    Optional<User> findByEmail(String email);

    Page<User> findByNameContaining(String name, Pageable pageable);
}
