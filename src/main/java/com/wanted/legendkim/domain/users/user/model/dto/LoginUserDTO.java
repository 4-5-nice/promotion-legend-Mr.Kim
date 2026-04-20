package com.wanted.legendkim.domain.users.user.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginUserDTO {

    private Long userId;
    private String email;
    private String password;
    private String name;
    private String role;
    private Integer point;
    private String rank;
    private Boolean isLocked;
    private Integer vacationCoupon;
    private Integer loginFailCount;
    private Boolean isPaid;

    public List<String> getRole(){
        if(this.role != null && this.role.length()>0){
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }
}
