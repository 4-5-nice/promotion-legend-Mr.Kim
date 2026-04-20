package com.wanted.legendkim.domain.lecture.dto;

import com.wanted.legendkim.domain.section.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LectureResponse {
    private Long lectureId;
    private String title;
    private String videoUrl;

    public static LectureResponse of(Section section) {
        return new LectureResponse(
                section.getId(),
                section.getTitle(),
                section.getVideoUrl()
        );
    }
}
