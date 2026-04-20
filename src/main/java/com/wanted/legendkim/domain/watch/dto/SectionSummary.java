package com.wanted.legendkim.domain.watch.dto;

import com.wanted.legendkim.domain.section.Section;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

/* comment.
    Section 엔티티는 DB 와 직집적으로 매핑되는 객체라서 함부로 외부에 노출하면 안된다.
    이로 인해 videoUrl, uploadSuccess, course 객체 참조까지 나오며 순환 참조의
    위험성도 있다. 따라서 시청 페이지에서 필요한 sectionId, title, videoUrl, note
    4개만 사용해서 필요한 필드만 골라서 전용 DTO 에 담는다.
 */
public class SectionSummary {

    private Long sectionId; // 시청 페이지의 JS 에서 섹션 클릭 시 어떤 섹션인지 식별하기 위해 필요
    private String title; // 시청 페이지 사이드바에서 섹션 목록 제목으로 랜더링
    private String videoUrl; // 태그의 src 에 넣어서 영상을 재생하기 위해 필요
    private String note; // 강의 노트 탭에 랜더링되는 텍스트 내용

    public static SectionSummary of(Section section) {
        return new SectionSummary(
                section.getId(),
                section.getTitle(),
                section.getVideoUrl(),
                section.getNote()
        );
    }
}
