package com.wanted.legendkim.domain.freeboard.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class FreeBoardDTO {

    private Long id;
    private String title;
    private String authorName;
    private Long viewCount;
    private String createdAt;
//    private boolean mine;
}