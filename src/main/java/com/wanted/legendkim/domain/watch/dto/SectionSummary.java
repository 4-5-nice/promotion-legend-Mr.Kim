package com.wanted.legendkim.domain.watch.dto;

import com.wanted.legendkim.domain.section.Section;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SectionSummary {

    private Long sectionId;
    private String title;
    private String videoUrl;

    public static SectionSummary of(Section section) {
        return new SectionSummary(
                section.getId(),
                section.getTitle(),
                section.getVideoUrl()
        );
    }
}
