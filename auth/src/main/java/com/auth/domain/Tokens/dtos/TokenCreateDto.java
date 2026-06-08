package com.auth.domain.Tokens.dtos;

import com.auth.domain.Tokens.enums.TokenType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenCreateDto {

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Token type is required")
    private TokenType tokenType;
}
