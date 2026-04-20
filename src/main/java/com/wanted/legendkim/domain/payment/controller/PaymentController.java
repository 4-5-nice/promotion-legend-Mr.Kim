package com.wanted.legendkim.domain.payment.controller;

import com.wanted.legendkim.domain.users.auth.model.dto.AuthDetails;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import com.wanted.legendkim.domain.users.user.model.dao.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final UserRepository userRepository;

    public PaymentController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 💡 1. 결제 페이지 보여주기
     * 기존 "payment/payment"에서 "user/payment"로 경로 수정
     */
    @GetMapping("/info")
    public String showPaymentPage() {
        return "user/payment";
    }

    /**
     * 💡 2. 결제 처리 및 DB 업데이트
     * @AuthenticationPrincipal을 통해 세션에서 로그인한 유저의 정보를 직접 가져옵니다.
     */
    @PostMapping("/process")
    @Transactional // 💡 데이터 변경을 위해 필수!
    public String processPayment(@AuthenticationPrincipal AuthDetails authDetails, Model model) {

        if (authDetails == null) {
            return "redirect:/auth/login";
        }

        Long userId = authDetails.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.isPaid(true);

        // 영수증 화면에 보여줄 가상 데이터 생성
        String receiptNo = "KBJ-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String paymentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));

        model.addAttribute("receiptNo", receiptNo);
        model.addAttribute("instructorName", "김부장");
        model.addAttribute("paymentDate", paymentDate);
        model.addAttribute("installment", "일시불");
        model.addAttribute("totalAmount", "₩12,000,000");


        return "user/payment-receipt";
    }
}