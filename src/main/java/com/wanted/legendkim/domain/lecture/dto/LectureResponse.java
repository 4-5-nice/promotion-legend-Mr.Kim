package com.wanted.legendkim.domain.lecture.dto;
// 강의 상세 조회 응답 DTO
// Section 엔티티를 Lecture 의미로 가공해 내려줌
import com.wanted.legendkim.domain.section.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LectureResponse {
    // 강의 재생에 필요한 최소 정보들
    private Long lectureId;
    private String title;
    private String videoUrl;

    // 내부는 Section 이라고 부르지만, 외부에는 강의라는 사용자 친화적 용어로 노출
    public static LectureResponse of(Section section) {
        return new LectureResponse(
                section.getId(),
                section.getTitle(),
                section.getVideoUrl()
        );
    }
}
