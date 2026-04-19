package com.wanted.legendkim.domain.users.user.model.dao;

import com.wanted.legendkim.domain.users.user.model.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginHistory, Long> {

    List<LoginHistory> findByUserId(Long userId);

    List<LoginHistory> findAllByOrderByCreatedAtDesc();

    List<LoginHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
