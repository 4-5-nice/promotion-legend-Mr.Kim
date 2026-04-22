package com.wanted.legendkim.domain.payment.controller;

import com.wanted.legendkim.domain.users.auth.model.dto.AuthDetails;
import com.wanted.legendkim.domain.users.user.model.entity.User;
import com.wanted.legendkim.domain.users.user.model.dao.UserRepository;

import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import com.wanted.legendkim.domain.mypage.entity.MPPayments;
import com.wanted.legendkim.domain.mypage.repository.UsersRepository;
import com.wanted.legendkim.domain.mypage.repository.PaymentsRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final UserRepository userRepository;

    private final UsersRepository mpUsersRepository;
    private final PaymentsRepository paymentsRepository;

    public PaymentController(UserRepository userRepository,
                             UsersRepository mpUsersRepository,
                             PaymentsRepository paymentsRepository) {
        this.userRepository = userRepository;
        this.mpUsersRepository = mpUsersRepository;
        this.paymentsRepository = paymentsRepository;
    }

    @GetMapping("/info")
    public String showPaymentPage() {
        return "user/payment";
    }

    @PostMapping("/process")
    @Transactional // 두 테이블(USERS, PAYMENTS)의 업데이트를 하나의 작업으로 묶음
    public String processPayment(@AuthenticationPrincipal AuthDetails authDetails, Model model) {

        if (authDetails == null) {
            return "redirect:/auth/login";
        }

        Long userId = authDetails.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.isPaid(true);

        // 마이페이지 도메인의 MPUsers 조회
        MPUsers mpUser = mpUsersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("마이페이지 유저 정보를 찾을 수 없습니다."));

        // MPPayments 객체 생성 및 저장
        int amount = 12000000;
        Date now = new Date();

        // MPPayments의 @AllArgsConstructor 활용해서 매개변수로 넘김
        MPPayments newPayment = new MPPayments(0, mpUser, amount, true, now);

        paymentsRepository.save(newPayment);

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