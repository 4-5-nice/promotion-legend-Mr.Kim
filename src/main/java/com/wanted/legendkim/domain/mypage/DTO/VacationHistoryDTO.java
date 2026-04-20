package com.wanted.legendkim.domain.mypage.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Service
@ToString
public class VacationHistoryDTO {
    private int vacationHistoryId;
    private int userId;
    private Date usedDate;
    private int deductedAmount;
    private String purpose;
    private String detailPurpose;
}
