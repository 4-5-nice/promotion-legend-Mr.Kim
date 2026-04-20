package com.wanted.legendkim.domain.watch.dto;

import com.wanted.legendkim.domain.enrollment.Enrollment;
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

    // 원본 Enrollment Entity 를 파라미터로 받아 필요한 값만 받아오기
    public static WatchInfoResponse of(Enrollment enrollment,
                                       List<SectionSummary> sections) {
        return new WatchInfoResponse(
                enrollment.getCourse().getTitle(), // Enrollment -> Course 객체 탐색으로 코스 제목 추출
                enrollment.getCourse().getInsName(), // Enrollment -> Course 객체 탐색으로 강사 이름 추출
                sections
        );
    }
}
