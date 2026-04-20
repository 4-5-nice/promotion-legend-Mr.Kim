package com.wanted.legendkim.domain.users.user.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PasswordResetDTO {

    private String email;
    private String identifyQuestion;
    private String identifyAnswer;
    private String newPassword;
    private String confirmPassword;
}
