package com.wanted.legendkim.domain.mypage.service;

import com.wanted.legendkim.domain.mypage.entity.MPEnrollments;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import com.wanted.legendkim.domain.mypage.repository.EnrollmentsRepository;
import com.wanted.legendkim.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentsService {

    private final EnrollmentsRepository enrollmentRepository;
    private final UsersRepository userRepository;

    // 모든 수강 내역을 가져와서 서버에서 미리 분류해버리기!

    // 1. 수강 중인 강의만 뽑아주는 메서드
    public List<MPEnrollments> getInProgressEnrollments(String email) {
        // 1. 리스트로 일단 받습니다. (레포지토리 수정 X)
        List<MPUsers> usersList = userRepository.findByEmail(email);

        // 2. 리스트가 비어있지 않다면 첫 번째 사람을 꺼냅니다.
        if (!usersList.isEmpty()) {
            MPUsers user = usersList.get(0);

            // 3. 찾은 유저로 수강 목록 필터링
            return enrollmentRepository.findByUserId(user).stream()
                    .filter(e -> "IN_PROGRESS".equals(e.getStatus()))
                    .collect(Collectors.toList());
        }

        // 유저가 없을 경우 예외 처리
        throw new IllegalArgumentException("해당 이메일의 유저를 찾을 수 없습니다: " + email);
    }

    // 2. 수강 완료된 강의만 뽑아주는 메서드
    public List<MPEnrollments> getCompletedEnrollments(String email) {
        // 1. 리스트로 일단 받습니다. (레포지토리 수정 X)
        List<MPUsers> usersList = userRepository.findByEmail(email);

        // 2. 리스트가 비어있지 않다면 첫 번째 사람을 꺼냅니다.
        if (!usersList.isEmpty()) {
            MPUsers user = usersList.get(0);

            // 3. 찾은 유저로 수강 목록 필터링
            return enrollmentRepository.findByUserId(user).stream()
                    .filter(e -> "COMPLETED".equals(e.getStatus()))
                    .collect(Collectors.toList());
        }

        // 유저가 없을 경우 예외 처리
        throw new IllegalArgumentException("해당 이메일의 유저를 찾을 수 없습니다: " + email);
    }

    @Transactional(readOnly = true)
    public List<MPEnrollments> appliedCourse(String loginId) { //신청한 강의 조회
        //사용자 정보 가져와서 리스트에 담기
        List<MPUsers> usersList = userRepository.findByEmail(loginId);
        if (!usersList.isEmpty()) { //리스트에 정보가 있다면
            MPUsers user = usersList.get(0); //리스트의 첫 번째 값 아이디를 user에 넣기
            List<MPEnrollments> list = enrollmentRepository.findByUserId(user); //아이디로 수강 정보 찾아서 리스트에 담기

            // 중요: 이 코드를 추가해서 Lazy 로딩을 강제로 실행시킵니다.
            for (MPEnrollments e : list) { //수강 정보 list를 반복하여 정보 찾기
                if (e.getCourseId() != null) { //코스 아이디가 null이 아니라면
                    e.getCourseId().getTitle(); // 강제 호출 //수강 테이블의 코스아이디와 일치하는 강의명을 강의 테이블에서 꺼내기
                }
            }
            return list;
        }
        throw new IllegalArgumentException("해당 이메일의 유저를 찾을 수 없습니다: " + loginId);
    }
}
