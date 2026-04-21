package com.wanted.legendkim.domain.users.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignupDTO {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthDate;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 16자 미만이어야 합니다.")
    private String password;

    @NotBlank
    private String identifyQuestion;

    @NotBlank
    private String identifyAnswer;
}
