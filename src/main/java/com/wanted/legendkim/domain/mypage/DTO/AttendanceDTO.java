package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceDTO {

    private int attendanceId;
    private int userId;
    private LocalDateTime targetDate;
    private String status; //PRESENT(출근), LATE(지각), ABSENT(결근), EXCUSED(공결)
}
