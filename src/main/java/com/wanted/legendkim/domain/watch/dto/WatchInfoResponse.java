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

    private String courseTitle;
    private String instructorName;
    private List<SectionSummary> sections;

    // 원본 Enrollment Entity 를 파라미터로 받아 필요한 값만 받아오기
    public static WatchInfoResponse of(Enrollment enrollment,
                                       List<SectionSummary> sections) {
        return new WatchInfoResponse(
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getInsName(),
                sections
        );
    }
}
