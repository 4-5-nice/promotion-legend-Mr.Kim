package com.wanted.legendkim.domain.mypage.DTO;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentsDTO {
    private int paymentId;
    private int user_id;
    private int amount;
    private boolean status;
    private Date createdAt;

}
