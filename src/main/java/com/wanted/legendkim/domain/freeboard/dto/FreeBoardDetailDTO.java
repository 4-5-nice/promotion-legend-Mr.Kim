package com.wanted.legendkim.domain.freeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FreeBoardDetailDTO {

    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long viewCount;
    private String createdAt;
    private boolean mine;

}
