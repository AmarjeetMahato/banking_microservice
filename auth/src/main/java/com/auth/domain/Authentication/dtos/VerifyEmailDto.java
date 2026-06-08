package com.auth.domain.Authentication.dtos;


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
}
