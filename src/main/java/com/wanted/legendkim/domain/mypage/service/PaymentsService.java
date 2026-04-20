package com.wanted.legendkim.domain.mypage.service;

import com.wanted.legendkim.domain.mypage.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentsService {
    private final PaymentsRepository paymentRepository;
}
