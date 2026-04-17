package com.wanted.legendkim.domain.watch.dto;

import com.wanted.legendkim.domain.enrollment.Enrollment;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WatchInfoResponse {

    private String courseTitle;
    private String instructorName;
    private List<SectionSummary> sections;

    public static WatchInfoResponse of(Enrollment enrollment,
                                       List<SectionSummary> sections) {
        return new WatchInfoResponse(
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getInsName(),
                sections
        );
    }
}
