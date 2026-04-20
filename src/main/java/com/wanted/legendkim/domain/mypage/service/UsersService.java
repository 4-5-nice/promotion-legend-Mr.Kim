package com.wanted.legendkim.domain.mypage.service;

import com.wanted.legendkim.domain.mypage.DTO.UsersDTO;
import com.wanted.legendkim.domain.mypage.entity.Attendance;
import com.wanted.legendkim.domain.mypage.entity.Payments;
import com.wanted.legendkim.domain.mypage.entity.Users;
import com.wanted.legendkim.domain.mypage.repository.AttendaceRepository;
import com.wanted.legendkim.domain.mypage.repository.PaymentsRepository;
import com.wanted.legendkim.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository userRepository;
    private final PaymentsRepository paymentRepository;
    private final AttendaceRepository attendaceRepository;

    public UsersDTO findByEmail(String loginId) {

        // 리스트로 조회 후 첫 번째 값을 가져오거나 에러 처리
        List<Users> usersList = userRepository.findByEmail(loginId);


        if (!usersList.isEmpty()) {
            Users user = usersList.get(0); // 리스트의 첫 번째 항목 추출

            //payments 테이블에서 결제 금액, 결제 날짜 가져오기.
            List<Payments> paymentsList = paymentRepository.findByUserId(user);

            // 엔티티를 DTO로 옮겨 담기
            return new UsersDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getPoint(),
                    user.getRank(),
                    user.getVacationCoupon(),
                    paymentsList
            );
        }
        return new UsersDTO("미확인사용자", loginId, 0, "직급없음", 0, new ArrayList<Payments>());
    }

    public UsersDTO findByTargetUserId(Long userId) {
        // 리스트로 조회 후 첫 번째 값을 가져오거나 에러 처리
        List<Users> usersList = userRepository.findByUserId(userId);


        if (!usersList.isEmpty()) {
            Users user = usersList.get(0); // 리스트의 첫 번째 항목 추출

            //payments 테이블에서 결제 금액, 결제 날짜 가져오기.
            List<Payments> paymentsList = paymentRepository.findByUserId(user);
            //attendance 테이블에서 출결 정보 가져오기
            List<Attendance> attendanceList = attendaceRepository.findByUserId(user);

            // 엔티티를 DTO로 옮겨 담기
            return new UsersDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getPoint(),
                    user.getRank(),
                    user.getVacationCoupon(),
                    paymentsList,
                    attendanceList
            );
        }
        return new UsersDTO("미확인사용자", "unknown@email.com", 0, "직급없음", 0, new ArrayList<Payments>(), new ArrayList<Attendance>());
    }
}
