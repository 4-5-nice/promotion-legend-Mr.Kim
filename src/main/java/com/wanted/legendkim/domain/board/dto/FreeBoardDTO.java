package com.wanted.legendkim.domain.board.dto;

import com.wanted.legendkim.domain.board.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class FreeBoardDTO {

    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long viewCount;
    private String createdAt;
    private boolean mine;

}