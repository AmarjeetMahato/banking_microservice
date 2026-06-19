package com.auth.domain.Authentication.dtos;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyEmailDto {

    @NotBlank(message = "Verification token is required")
    private String token;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private  String email;
}
