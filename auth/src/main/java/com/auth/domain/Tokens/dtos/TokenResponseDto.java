package com.auth.domain.Tokens.dtos;


import com.auth.domain.Tokens.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDto {

    private String id; // BaseEntity se agar aap primary key expose karna chahein
    private String tokenValue;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
    private LocalDateTime expiryDate;
    private String userId; // Sirf User ki ID bhej rahe hain, poora object nahi
}
