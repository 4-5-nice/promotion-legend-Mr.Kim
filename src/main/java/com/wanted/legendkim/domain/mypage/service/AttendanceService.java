package com.wanted.legendkim.domain.mypage.service;

import com.wanted.legendkim.domain.mypage.entity.MPAttendance;
import com.wanted.legendkim.domain.mypage.entity.MPUsers;
import com.wanted.legendkim.domain.mypage.entity.MPVacationHistory;
import com.wanted.legendkim.domain.mypage.repository.AttendanceRepository;
import com.wanted.legendkim.domain.mypage.repository.UsersRepository;
import com.wanted.legendkim.domain.mypage.repository.VacationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UsersRepository userRepository;
    private final VacationHistoryRepository vacationHistoryRepository;

    public List<MPAttendance> attendanceList(String email) {
        // 1. 기존 Repository 메서드 그대로 사용 (List 반환)
        List<MPUsers> userList = userRepository.findByEmail(email);

        // 2. 리스트가 비어있지 않은지 확인하고 첫 번째 유저 추출
        if (userList != null && !userList.isEmpty()) {
            MPUsers user = userList.get(0); // 첫 번째 유저를 가져옵니다.
            return attendanceRepository.findByUserId(user);
        }

        // 3. 유저를 못 찾았다면 빈 리스트 반환
        return new ArrayList<>();
    }

    public Map<String, Long> getAttendanceInfo(List<MPAttendance> list) {
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
    public List<MPAttendance> attendanceListById(Long userId) {
        // 1. Repository에서 리스트로 유저를 찾습니다.
        List<MPUsers> users = userRepository.findByUserId(userId);

        // 2. 리스트가 비어있지 않다면 첫 번째 유저의 출결 정보를 가져옵니다.
        if (users != null && !users.isEmpty()) {
            MPUsers user = users.get(0); // 리스트에서 유저 한 명 추출
            return attendanceRepository.findByUserId(user);
        }

        // 3. 없으면 깔끔하게 빈 리스트 반환
        return new ArrayList<>();
    }

    @Transactional
    public boolean updateAttendanceStatus(List<Map<String, Object>> updateList) {
        if (updateList == null || updateList.isEmpty()) return false;

        // [중요] 0번째 데이터의 userId가 0이면, 다른 데이터에서라도 찾아야 합니다.
        int requestUserId = 0;
        for(Map<String, Object> data : updateList) {
            int id = Integer.parseInt(String.valueOf(data.get("userId")));
            if(id != 0) {
                requestUserId = id;
                break;
            }
        }

        if(requestUserId == 0) {
            System.out.println("❌ 에러: 모든 요청 데이터의 userId가 0입니다. 수정을 중단합니다.");
            return false;
        }

        // 첫 번째 데이터에서 userId 추출
//        requestUserId = Integer.parseInt(String.valueOf(updateList.get(0).get("userId")));

        // 해당 유저의 기록을 긁어옴
        List<MPAttendance> dbRecords = attendanceRepository.findByUserId_UserId(requestUserId);

        for (Map<String, Object> reqData : updateList) {
            // [수정] 데이터 변환 시 발생할 수 있는 소수점/타입 오류 방지
            int reqId = (int) Double.parseDouble(String.valueOf(reqData.get("attendanceId")));
            String reqDate = (String) reqData.get("targetDate");
            String reqStatus = (String) reqData.get("status");

            for (MPAttendance dbRecord : dbRecords) {
                // 1. PK 비교 (int vs int)
                boolean isSameId = (dbRecord.getAttendanceId() == reqId);

                // 2. 유저 ID 비교 (Lazy 로딩 방지 위해 ID 직접 비교)
                boolean isSameUser = (dbRecord.getUserId().getUserId() == requestUserId);

                // 3. 날짜 비교 (LocalDateTime -> LocalDate -> String)
                String dbDateOnly = dbRecord.getTargetDate().toLocalDate().toString();
                boolean isSameDate = dbDateOnly.equals(reqDate);

                // 매칭 전 검증 로그 추가
                if (reqData.get("attendanceId") == null) {
                    System.out.println("⚠️ 경고: 요청 데이터에 ID가 없습니다! 날짜: " + reqDate);
                    continue; // ID 없으면 그냥 이번 루프 건너뛰게 하세요. 그래야 INSERT가 안 생깁니다.
                }
                // [테스트 로그 - 실행 후 콘솔을 보세요!]
                System.out.println("ID 비교: DB=" + dbRecord.getAttendanceId() + " / REQ=" + reqId + " -> " + isSameId);
                System.out.println("날짜 비교: DB=" + dbDateOnly + " / REQ=" + reqDate + " -> " + isSameDate);
                System.out.println("결과: " + (isSameId && isSameUser && isSameDate ? "매칭 성공! 수정함" : "매칭 실패!"));

                // 삼중 필터가 하나라도 안 맞으면 JPA는 수정을 안 합니다.
                if (isSameId && isSameUser && isSameDate) {
                    dbRecord.changeStatus(reqStatus);
                    // dirty checking으로 여기서 끝내야 합니다.
                    break;
                }
            }
        }
        return true;
    }

    @Transactional
    public boolean registerVacation(String loginId, Map<String, Object> data) {
        try {
            String purpose = (String) data.get("purpose");
            // 1. 상세 사유 추가 (HTML textarea에서 보낸 값)
            String detailPurpose = (String) data.get("detailPurpose");
            List<String> dateList = (List<String>) data.get("dateList"); // ["2026-04-20", ...]

            // 유저 정보 가져오기
            List<MPUsers> userList = userRepository.findByEmail(loginId);
            if (userList.isEmpty()) return false;
            MPUsers user = userList.get(0);

            int useCount = dateList.size();
            LocalDate today = LocalDate.now();

            for (String dateStr : dateList) {
                LocalDate targetDate = LocalDate.parse(dateStr);
                if (targetDate.isBefore(today)) return false;

                // 2. 연차 이력 저장 (VacationHistory)
                // TODO: VacationHistory 엔티티에 detailPurpose 컬럼을 만드셨다면
                // 아래 fillDetails 메서드나 setter에 추가해주셔야 저장됩니다!
                java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr);
                MPVacationHistory history = new MPVacationHistory()
                        .fillDetails(user, sqlDate, 1, purpose, detailPurpose);
                vacationHistoryRepository.save(history);

                // 3. [핵심] 출결(Attendance) 테이블에도 데이터 추가!
                // 시간을 00:00:00으로 맞추기 위해 .atStartOfDay() 사용
                MPAttendance attendance = new MPAttendance();
                attendance.fillDetails(
                        user,
                        targetDate.atStartOfDay(), // 부장님 말씀대로 00:00:00 세팅
                        "EXCUSED"                  // 연차니까 '공결' 상태로 고정
                );
                attendanceRepository.save(attendance);
            }

            // 4. 원래 연차에서 개수 차감
            user.useVacation(useCount);
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
