package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EnrollmentsDTO {
    private int enrollmentId;
    private int userId;
    private int courseId;
    private String status;
    private Date dealineDate;
    private Date startAt;
    private Date finishDate;
    private int progress;
}
