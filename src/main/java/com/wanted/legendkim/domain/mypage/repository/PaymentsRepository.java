package com.wanted.legendkim.domain.mypage.repository;

import com.wanted.legendkim.domain.mypage.entity.MPPayments;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<MPPayments, Integer> {
    List<MPPayments> findByUserId(MPUsers userId);

//    Payments findByPaymentId(int paymentId);

    void deleteByUserId(MPUsers user);
}
