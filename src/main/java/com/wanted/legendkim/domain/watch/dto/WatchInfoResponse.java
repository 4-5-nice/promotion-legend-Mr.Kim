package com.wanted.legendkim.domain.watch.dto;

import com.wanted.legendkim.domain.enrollment.entity.Enrollment;
import lombok.*;

import java.util.List;

// lombok 사용
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WatchInfoResponse {

    // 시청페이지 상단에 현재 수강 중인 코스 제목을 랜더링 하는 목적
    private String courseTitle;
    // 시청 페이지에 강사 이름을 표시하기 위해서 사용
    private String instructorName;
    // 시청 페이지 사이드바에서 섹션 목록을 랜더링하기 위해 필요
    private List<SectionSummary> sections;
    // 직급별 재생 속도 제한 적용을 위해 사용자의 직급 정보를 Thymeleaf 로 전달하는 필드
    private String userRank;

    // 원본 Enrollment Entity 를 파라미터로 받아 필요한 값만 추출해 DTO 를 생성하는 정적 팩토리 메서드
    // userRank 는 WatchService 에서 User 를 별도 조회한 뒤 주입한다.
    public static WatchInfoResponse of(Enrollment enrollment,
                                       List<SectionSummary> sections,
                                       String userRank) {
        return new WatchInfoResponse(
                enrollment.getCourse().getTitle(),   // Enrollment -> Course 객체 탐색으로 코스 제목 추출
                enrollment.getCourse().getInsName(), // Enrollment -> Course 객체 탐색으로 강사 이름 추출
                sections,
                userRank                             // User 엔티티에서 가져온 직급 값
        );
    }
}
