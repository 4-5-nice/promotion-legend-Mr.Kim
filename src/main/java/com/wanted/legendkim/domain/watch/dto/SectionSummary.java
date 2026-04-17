package com.wanted.legendkim.domain.watch.dto;

import com.wanted.legendkim.domain.section.Section;
import lombok.*;

// lombok 세팅
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SectionSummary {

    private Long sectionId;
    private String title;
    private String videoUrl;

    // 원본 Section Entity 를 파라미터로 받아 필요한 값만 조합한다.
    public static SectionSummary of(Section section) {
        return new SectionSummary(
                section.getId(),
                section.getTitle(),
                section.getVideoUrl()
        );
    }
}
