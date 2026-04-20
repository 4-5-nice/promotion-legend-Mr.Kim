package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.Payments;
import com.wanted.legendkim.domain.mypage.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Integer> {
    List<Payments> findByUserId(Users userId);

    Payments findByPaymentId(int paymentId);
}
