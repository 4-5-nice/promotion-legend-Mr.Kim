package com.wanted.legendkim.domain.mypage.service;

import com.wanted.legendkim.domain.mypage.entity.Attendance;
import com.wanted.legendkim.domain.mypage.entity.Users;
import com.wanted.legendkim.domain.mypage.repository.AttendaceRepository;
import com.wanted.legendkim.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendaceRepository attendaceRepository;
    private final UsersRepository userRepository;

    public List<Attendance> attendanceList(String email) {
        // 1. 기존 Repository 메서드 그대로 사용 (List 반환)
        List<Users> userList = userRepository.findByEmail(email);

        // 2. 리스트가 비어있지 않은지 확인하고 첫 번째 유저 추출
        if (userList != null && !userList.isEmpty()) {
            Users user = userList.get(0); // 첫 번째 유저를 가져옵니다.
            return attendaceRepository.findByUserId(user);
        }

        // 3. 유저를 못 찾았다면 빈 리스트 반환
        return new ArrayList<>();
    }

    public Map<String, Long> getAttendanceInfo(List<Attendance> list) {
        Map<String, Long> stats = new HashMap<>();

        // 출석(PRESENT), 지각(LATE), 결근(ABSENT), 공결(OFFICIAL) 등의 상태가 Enum에 있다고 가정합니다.
        long presentCount = list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
        long lateCount = list.stream().filter(a -> "LATE".equals(a.getStatus())).count();
        long absentCount = list.stream().filter(a -> "ABSENT".equals(a.getStatus())).count();
        long excusedCount = list.stream().filter(a -> "EXCUSED".equals(a.getStatus())).count();

        // 부장님 요청: 총 출근일 = 출석 + 공결
        stats.put("totalAttendance", presentCount + lateCount + excusedCount);
        stats.put("lateCount", lateCount);
        stats.put("absentCount", absentCount);

        return stats;
    }

    // 관리자용: userId(Long)로 출결 리스트 조회
    public List<Attendance> attendanceListById(Long userId) {
        // 1. Repository에서 리스트로 유저를 찾습니다.
        List<Users> users = userRepository.findByUserId(userId);

        // 2. 리스트가 비어있지 않다면 첫 번째 유저의 출결 정보를 가져옵니다.
        if (users != null && !users.isEmpty()) {
            Users user = users.get(0); // 리스트에서 유저 한 명 추출
            return attendaceRepository.findByUserId(user);
        }

        // 3. 없으면 깔끔하게 빈 리스트 반환
        return new ArrayList<>();
    }
}
