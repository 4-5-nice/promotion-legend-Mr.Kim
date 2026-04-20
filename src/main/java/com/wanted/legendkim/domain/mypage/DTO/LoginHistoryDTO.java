package com.wanted.legendkim.domain.mypage.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Service
@ToString
public class LoginHistoryDTO {
    private int historyId;
    private int userId;
    private boolean isSuccess;
    private String failReason;
    private LocalDateTime createdAt;
}
