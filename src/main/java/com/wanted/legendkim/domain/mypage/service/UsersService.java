package com.wanted.legendkim.domain.mypage.service;

import com.wanted.legendkim.domain.mypage.entity.MPAttendance;
import com.wanted.legendkim.domain.mypage.entity.MPPayments;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;

import com.wanted.legendkim.domain.mypage.DTO.UsersDTO;
import com.wanted.legendkim.domain.mypage.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository userRepository;
    private final PaymentsRepository paymentRepository;
    private final AttendanceRepository attendanceRepository;
    private final SectionProgressRepository sectionProgressRepository;
    private final EnrollmentsRepository enrollmentRepository;
    private final QuestionSubmissionsRepository questionSubmissionRepository;
    private final CommentsRepository commentRepository;
    private final CoursesRepository courseRepository;
    private final FreeBoardsRepository freeboardRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final QuestionsRepository questionRepository;
    private final SectionsRepository sectionRepository;
    private final VacationHistoryRepository vacationHistoryRepository;

    public UsersDTO findByEmail(String loginId) {

        // 리스트로 조회 후 첫 번째 값을 가져오거나 에러 처리
        List<MPUsers> usersList = userRepository.findByEmail(loginId);


        if (!usersList.isEmpty()) {
            MPUsers user = usersList.get(0); // 리스트의 첫 번째 항목 추출

            //로그인 사용자를 기준으로 payments 테이블에서 결제 금액, 결제 날짜 가져오기.
            List<MPPayments> paymentsList = paymentRepository.findByUserId(user);

            // 엔티티를 DTO로 옮겨 담기
            return new UsersDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getPoint(),
                    user.getRank(),
                    user.getVacationCoupon(),
                    paymentsList //결제 관련 정보
            );
        }
        return new UsersDTO("미확인사용자", loginId, 0, "직급없음", 0, new ArrayList<MPPayments>());
    }

    public UsersDTO findByTargetUserId(Long userId) {
        // 리스트로 조회 후 첫 번째 값을 가져오거나 에러 처리
        List<MPUsers> usersList = userRepository.findByUserId(userId);


        if (!usersList.isEmpty()) {
            MPUsers user = usersList.get(0); // 리스트의 첫 번째 항목 추출

            //payments 테이블에서 결제 금액, 결제 날짜 가져오기.
            List<MPPayments> paymentsList = paymentRepository.findByUserId(user);
            //attendance 테이블에서 출결 정보 가져오기
            List<MPAttendance> attendanceList = attendanceRepository.findByUserId(user);

            // 엔티티를 DTO로 옮겨 담기
            UsersDTO dto = new UsersDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getPoint(),
                    user.getRank(),
                    user.getVacationCoupon(),
                    paymentsList,
                    attendanceList
            );

            dto.setUserId(user.getUserId());

            return dto;

        }
        return new UsersDTO("미확인사용자", "unknown@email.com", 0, "직급없음", 0, new ArrayList<MPPayments>(), new ArrayList<MPAttendance>());
    }

    //회원 탈퇴
    @Transactional
    public void deleteUserAllData(String loginId) {
        //로그인한 사용자 정보
        MPUsers user = userRepository.findByEmail(loginId).get(0);

        // 🚩 1. 남들이 내 콘텐츠에 남긴 흔적부터 소탕 (안 그러면 내 글이 안 지워짐)
        commentRepository.deleteByPostId_UserId(user);        // 내 자유게시판 글에 남들이 단 댓글
        commentRepository.deleteByQuestionId_UserId(user);    // 내 퀴즈에 남들이 단 댓글
        questionSubmissionRepository.deleteByQuestionId_UserId(user); // 내 퀴즈에 남들이 낸 답안지

        // 🚩 2. 내가 만든 '부모' 데이터들 삭제
        // (이미 1번에서 꼬리표들을 지웠으니 이제 게시글과 퀴즈가 시원하게 지워집니다)
        freeboardRepository.deleteByUserId(user);
        questionRepository.deleteByUserId(user);

        // 🚩 3. 유저 개인 활동 기록 삭제
        sectionProgressRepository.deleteByEnrollmentId_UserId(user); // 수강 진도
        enrollmentRepository.deleteByUserId(user);      // 수강 신청
        commentRepository.deleteByUserId(user);         // 내가 쓴 댓글
        questionSubmissionRepository.deleteByUserId(user); // 내가 푼 문제

        paymentRepository.deleteByUserId(user);
        attendanceRepository.deleteByUserId(user);
        vacationHistoryRepository.deleteByUserId(user);
        loginHistoryRepository.deleteByUserId(user);

        // 🚩 4. 강의실 및 섹션
        sectionRepository.deleteByCourseId_UserId(user);
        courseRepository.deleteByUserId(user);

        // 🚩 5. 마지막 주인공 삭제
        userRepository.delete(user);
    }
}
